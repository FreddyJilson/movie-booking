package link.shortener.repository;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import org.mapdb.*;

import javax.annotation.PostConstruct;

@Slf4j
@Repository
public class MapDBRepositoryImpl implements MapDBRepository<String, String> {
    DB db;

    @PostConstruct
    void initialize() {
        try {
            db = DBMaker.fileDB("file.db").transactionEnable().make();
        } catch (Exception ex) {
            log.error("Error initializng Mapdb, check configurations and permissions, exception: {}, message: {}, stackTrace: {}",
                    ex.getCause(), ex.getMessage(), ex.getStackTrace());
        }
        log.info("MapDb initialized and ready to use");
    }

    @Override
    public synchronized DB beginTransaction() {
        log.info("beginTransaction");
        try {
            return db;
        } catch (Exception e) {
            log.error("Error saving entry in MapsDb, cause: {}, message: {}", e.getCause(), e.getMessage());
        }

        return db;
    }

    @Override
    public synchronized void save(DB tx, String key, String value) {
        log.info("save");
        try {
            ConcurrentNavigableMap<String, String> map = tx
                    .treeMap("movieBooking", Serializer.STRING, Serializer.STRING)
                    .createOrOpen();

            map.put(key, value);
        } catch (Exception e) {
            log.error("Error saving entry in MapsDb, cause: {}, message: {}", e.getCause(), e.getMessage());
        }
    }

    @Override
    public synchronized NavigableSet<String> find(DB tx) {
        log.info("findAll");
        ConcurrentNavigableMap<String, String> map = null;
        NavigableSet<String> keys = null;
        try {
            map = tx.treeMap("movieBooking", Serializer.STRING, Serializer.STRING).createOrOpen();
            return map.keySet();

        } catch (Exception e) {
            log.error("Error saving entry in MapsDb, cause: {}, message: {}", e.getCause(), e.getMessage());
        }

        return map.keySet();
    }

    @Override
    public synchronized String find(DB tx, String key) {
        log.info("find");
        ConcurrentNavigableMap<String, String> map = null;
        try {
            map = tx.treeMap("movieBooking", Serializer.STRING, Serializer.STRING).createOrOpen();
            return map.get(key);
        } catch (Exception e) {
            log.error("Error saving entry in MapsDb, cause: {}, message: {}", e.getCause(), e.getMessage());
        }

        return map.get(key);
    }

}