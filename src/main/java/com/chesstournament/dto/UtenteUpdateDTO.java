package com.chesstournament.dto;

import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Utente;
import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtenteUpdateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    private List<RuoloDTO> ruolo;
    private Long[] ruoliIds;

    public Utente buildUtenteModel(Long id) {
        Utente model = new Utente();
        model.setId(id);
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        return model;
    }

    public List<Ruolo> getRuoli() {
        if (ruoliIds != null) {
            return Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toList());
        }
        if (ruolo != null) {
            return ruolo.stream()
                    .map(item -> item.getId() == null ? null : new Ruolo(item.getId()))
                    .filter(item -> item != null)
                    .collect(Collectors.toList());
        }
        return null;
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

    public Long[] getRuoliIds() {
        return ruoliIds;
    }

    public void setRuoliIds(Long[] ruoliIds) {
        this.ruoliIds = ruoliIds;
    }

    public List<RuoloDTO> getRuolo() {
        return ruolo;
    }

    public void setRuolo(List<RuoloDTO> ruolo) {
        this.ruolo = ruolo;
    }
}
