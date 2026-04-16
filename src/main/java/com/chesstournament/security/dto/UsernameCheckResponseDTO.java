package com.chesstournament.security.dto;

import java.util.List;

public class UsernameCheckResponseDTO {

    private boolean available;
    private List<String> suggeriti;

    public UsernameCheckResponseDTO() {
    }

    public UsernameCheckResponseDTO(boolean available, List<String> suggeriti) {
        this.available = available;
        this.suggeriti = suggeriti;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getSuggeriti() {
        return suggeriti;
    }

    public void setSuggeriti(List<String> suggeriti) {
        this.suggeriti = suggeriti;
    }
}
