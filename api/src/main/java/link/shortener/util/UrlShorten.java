package link.shortener.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class UrlShorten {
    // https://www.geeksforgeeks.org/sha-512-hash-in-java/
    public static String encryptThisUrl(String input)
    {
        try {
            // To get unique url link hash
            long unixTimestamp = Instant.now().getEpochSecond();
            input = input+Long.toString(unixTimestamp);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the shorten url link
            return hashtext.substring(0, 8);
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
