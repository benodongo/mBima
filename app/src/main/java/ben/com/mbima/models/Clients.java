package ben.com.mbima.models;

/**
 * Created by benson on 4/23/18.
 */

public class Clients {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String insurance_co;
    private String date;
    private String policy_type;
    private String premium;
    private String policy_no;
    private String duration;
    private String expiry_date;
    private String due_time;
    //constructor

    public Clients() {
    }

    public Clients(String id, String firstname, String insurance_co, String date, String policy_type, String premium, String policy_no) {
        this.id = id;
        this.firstname = firstname;
        this.insurance_co = insurance_co;
        this.date = date;
        this.policy_type = policy_type;
        this.premium = premium;
        this.policy_no = policy_no;
    }

    public Clients(String id, String firstname, String lastname, String email, String phone, String insurance_co, String date, String policy_type, String premium, String policy_no, String duration) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.insurance_co = insurance_co;
        this.date = date;
        this.policy_type = policy_type;
        this.premium = premium;
        this.policy_no = policy_no;
        this.duration = duration;
    }


//getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsurance_co() {
        return insurance_co;
    }

    public void setInsurance_co(String insurance_co) {
        this.insurance_co = insurance_co;
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

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }
}
