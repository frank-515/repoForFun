package pojo;
import util.shaDigest;
public class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = shaDigest.toSHA(password + this.username);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = shaDigest.toSHA(password + username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
