import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class TestHash {
    @Test
    public void test1() {
        String str = "frank515";
        System.out.println(toSHA("frank515"));
    }

    private String toSHA(String s) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes(StandardCharsets.UTF_8));
            return new String(digest.digest(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "Error.";
    }
}