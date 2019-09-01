package styles.zonetech.net.styles.server.Models;

public class User {
    String email,password,id,saloonEnName,saloonArName,phone,userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSaloonEnName() {
        return saloonEnName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSaloonEnName(String saloonEnName) {
        this.saloonEnName = saloonEnName;
    }

    public String getSaloonArName() {
        return saloonArName;
    }

    public void setSaloonArName(String saloonArName) {
        this.saloonArName = saloonArName;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
