package ben.com.mbima.models;

public class User {
    public String id;
    public String userName;
    public String email;
    public String password;
    public String phone;

    public User(String id, String userName, String email, String password, String phone) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

}
