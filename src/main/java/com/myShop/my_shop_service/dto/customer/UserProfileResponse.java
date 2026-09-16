package com.myShop.my_shop_service.dto.customer;

public class UserProfileResponse {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private String role;

    public UserProfileResponse() {
    }

    public UserProfileResponse(
            Long id,
            String name,
            String mobileNumber,
            String email,
            String role
    ) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}