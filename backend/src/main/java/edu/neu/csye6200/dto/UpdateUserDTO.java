package edu.neu.csye6200.dto;

public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String bio;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    private String tier;
    private Long walletCents;

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Long getWalletCents() {
        return walletCents;
    }

    public void setWalletCents(Long walletCents) {
        this.walletCents = walletCents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
