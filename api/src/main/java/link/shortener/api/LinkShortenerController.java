package link.shortener.api;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

import link.shortener.util.UrlShorten;
import org.mapdb.DB;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import link.shortener.model.LinkShortenForm;
import link.shortener.repository.MapDBRepository;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping("/api/v1")
public class LinkShortenerController {
    private final MapDBRepository<String, String> mapDb;

    public LinkShortenerController(MapDBRepository<String, String> mapDb) {
        this.mapDb = mapDb;
    }

    @PostMapping(value = "/shorten-link", consumes = "application/json", produces = "application/json")
    public ResponseEntity<byte[]> shortenUrl(@RequestBody String linkShortenForm) {
        DB tx = mapDb.beginTransaction();

        LinkShortenForm form = new LinkShortenForm();
        ObjectMapper mapper = new ObjectMapper();
        try {
            form = mapper.readValue(linkShortenForm, LinkShortenForm.class);
        } catch (JacksonException e) {
            tx.rollback();

            log.error("Could not decode body form: " + e.getCause(), e.getMessage());

            return new ResponseEntity<>("Could not process the request body form".getBytes(), HttpStatus.BAD_REQUEST);
        }

        if (form.getLink().equals("")) {
            tx.rollback();

            return new ResponseEntity<>("Url link not send".getBytes(), HttpStatus.BAD_REQUEST);
        }

        try {
            while (true) {
                String redirectLink = form.getLink();
                String urlShortened = UrlShorten.encryptThisUrl(redirectLink);
                String redirectUrlCheckStr = mapDb.find(tx, urlShortened);
                if (redirectUrlCheckStr == "" || redirectUrlCheckStr == null) {
                    mapDb.save(tx, urlShortened, redirectLink);
                    log.info("MabsDbApi.save");

                    LinkShortenForm response = new LinkShortenForm();
                    response.link = urlShortened;
                    String responseJsonStr = mapper.writeValueAsString(response);

                    tx.commit();
                    return new ResponseEntity<>(responseJsonStr.getBytes(), HttpStatus.CREATED);
                }
            }
        } catch (Exception e) {
            tx.rollback();
            log.error("Could not save data: " + e.getCause(), e.getMessage());

            return new ResponseEntity<>("Could not save data".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/redirect-url", consumes = "application/json", produces = "application/json")
    public ResponseEntity<byte[]> redirectUrlLink(@RequestBody String linkShortenForm) {
        log.info("MabsDbApi.find");

        DB tx = mapDb.beginTransaction();

        LinkShortenForm form = new LinkShortenForm();
        ObjectMapper mapper = new ObjectMapper();
        try {
            form = mapper.readValue(linkShortenForm, LinkShortenForm.class);
        } catch (JacksonException e) {
            tx.rollback();

            log.error("Could not decode body form: " + e.getCause(), e.getMessage());

            return new ResponseEntity<>("Could not process the request body form".getBytes(), HttpStatus.BAD_REQUEST);
        }

        try {
            String redirectUrl = mapDb.find(tx, form.link);
            if (redirectUrl == "" || redirectUrl == null) {
                tx.rollback();

                return new ResponseEntity<>("There was no redirect url found".getBytes(), HttpStatus.NOT_FOUND);
            }

            LinkShortenForm response = new LinkShortenForm();
            response.link = redirectUrl;
            String responseJsonStr = mapper.writeValueAsString(response);

            tx.commit();

            return new ResponseEntity<>(responseJsonStr.getBytes(), HttpStatus.OK);
        } catch (Exception e) {
            tx.rollback();
            log.error("Could not get data: " + e.getCause(), e.getMessage());

            return new ResponseEntity<>("Could not get data".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}