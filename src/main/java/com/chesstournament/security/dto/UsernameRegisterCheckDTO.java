package com.chesstournament.security.dto;

import jakarta.validation.constraints.NotBlank;

public class UsernameRegisterCheckDTO {
    @NotBlank(message = "Lo username è obbligatorio")
    private String username;

    public UsernameRegisterCheckDTO() {
    }

    public UsernameRegisterCheckDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
