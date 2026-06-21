package com.podcastmanagement.dto.request;

public class ProfileRequest {

    private String name;
    private String notableWork;
    private String experience;
    private String phone;
    private String password;

    public ProfileRequest() {}

    public ProfileRequest(String name, String notableWork, String experience, String phone, String password) {
        this.name = name;
        this.notableWork = notableWork;
        this.experience = experience;
        this.phone = phone;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNotableWork() { return notableWork; }
    public void setNotableWork(String notableWork) { this.notableWork = notableWork; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
