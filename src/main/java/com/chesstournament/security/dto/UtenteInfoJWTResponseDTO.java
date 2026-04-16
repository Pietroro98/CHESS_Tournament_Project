package com.chesstournament.security.dto;

import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.model.StatoUtente;

import java.time.LocalDate;
import java.util.List;

public class UtenteInfoJWTResponseDTO {

    private String nome;
    private String cognome;
    private String type = "Bearer";
    private String username;
    private StatoUtente stato;
    private Integer eloRating;
    private Double montePremi;
    private TorneoDTO torneo;
    private List<TorneoDTO> torneiCreati;
    private List<String> roles;
    private LocalDate dataRegistrazione;

    public UtenteInfoJWTResponseDTO(String nome, String cognome, String username, StatoUtente stato,
                                    Integer eloRating, Double montePremi, TorneoDTO torneo,
                                    List<TorneoDTO> torneiCreati, List<String> roles, LocalDate dataRegistrazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.stato = stato;
        this.eloRating = eloRating;
        this.montePremi = montePremi;
        this.torneo = torneo;
        this.torneiCreati = torneiCreati;
        this.roles = roles;
        this.dataRegistrazione = dataRegistrazione;
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

    public void setRoles(List<String> roles) {
        this.roles = roles;
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

    public StatoUtente getStato() {
        return stato;
    }

    public void setStato(StatoUtente stato) {
        this.stato = stato;
    }

    public Integer getEloRating() {
        return eloRating;
    }

    public void setEloRating(Integer eloRating) {
        this.eloRating = eloRating;
    }

    public Double getMontePremi() {
        return montePremi;
    }

    public void setMontePremi(Double montePremi) {
        this.montePremi = montePremi;
    }

    public TorneoDTO getTorneo() {
        return torneo;
    }

    public void setTorneo(TorneoDTO torneo) {
        this.torneo = torneo;
    }

    public List<TorneoDTO> getTorneiCreati() {
        return torneiCreati;
    }

    public void setTorneiCreati(List<TorneoDTO> torneiCreati) {
        this.torneiCreati = torneiCreati;
    }

    public LocalDate getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(LocalDate dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
}
