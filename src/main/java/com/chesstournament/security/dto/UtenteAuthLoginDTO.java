package com.chesstournament.security.dto;

import jakarta.validation.constraints.NotBlank;

public class UtenteAuthLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UtenteAuthLoginDTO() {
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
