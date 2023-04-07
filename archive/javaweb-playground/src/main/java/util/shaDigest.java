package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class shaDigest {
    public static String toSHA(String str) {
        try {
            str = str + "frank515" + str;
            var digest = MessageDigest.getInstance("SHA-256");
            digest.update(str.getBytes(StandardCharsets.UTF_8));
            return new String(digest.digest(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "Error.";
    }
}
