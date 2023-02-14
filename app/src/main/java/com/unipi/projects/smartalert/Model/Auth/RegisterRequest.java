package com.unipi.projects.smartalert.Model.Auth;

public class RegisterRequest {
    private String email;

    private String phone;
    private String password;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getPhone() { return phone; }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhone(String phone) { this.phone = phone; }
}
