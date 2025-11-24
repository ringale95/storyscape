package edu.neu.csye6200.dto;

import edu.neu.csye6200.entity.User;

public class UserProfileDTO {
    public long id;
    public String firstName;
    public String lastName;
    public String email;
    public String tier;
    public Long walletCents;
    public Double walletDollars;

    public UserProfileDTO(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.tier = user.getTier().name();
        this.id = user.getId();
        this.walletCents = user.getWalletCents();
        this.walletDollars = user.getWalletCents() / 100.0;
    }
}
