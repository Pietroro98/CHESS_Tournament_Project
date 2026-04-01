package com.chesstournament.security.dto;

import java.util.List;

public class UtenteAuthJWTResponseDTO {

    private String token;
    private String type = "Bearer";
    private String username;
    private List<String> roles;

    public UtenteAuthJWTResponseDTO(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
