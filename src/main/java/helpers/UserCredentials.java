package helpers;

import models.User;

public class UserCredentials {
    private String name;
    private String email;
    private String password;

    private UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public String getLogin() {
        return email;
    }

    public void setLogin(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}