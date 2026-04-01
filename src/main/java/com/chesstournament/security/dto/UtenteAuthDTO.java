package com.chesstournament.security.dto;

import jakarta.validation.constraints.NotBlank;

public class UtenteAuthDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UtenteAuthDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
