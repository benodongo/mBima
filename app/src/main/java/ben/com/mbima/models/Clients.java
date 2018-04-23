package ben.com.mbima.models;

/**
 * Created by benson on 4/23/18.
 */

public class Clients {
    private String name;
    private String email;
    private String password;
    private String date;
    private String policy_type;
    private String policy_cost;
    private String due_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPolicy_type() {
        return policy_type;
    }

    public void setPolicy_type(String policy_type) {
        this.policy_type = policy_type;
    }

    public String getPolicy_cost() {
        return policy_cost;
    }

    public void setPolicy_cost(String policy_cost) {
        this.policy_cost = policy_cost;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
}
