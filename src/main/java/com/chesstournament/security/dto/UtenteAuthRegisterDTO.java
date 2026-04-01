package com.chesstournament.security.dto;

import com.chesstournament.model.Utente;
import jakarta.validation.constraints.NotBlank;

public class UtenteAuthRegisterDTO {

    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UtenteAuthRegisterDTO() {
    }

    public Utente buildUtenteModel() {
        Utente model = new Utente();
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        model.setPassword(password);
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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
