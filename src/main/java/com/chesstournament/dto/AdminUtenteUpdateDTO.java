package com.chesstournament.dto;

import com.chesstournament.model.Ruolo;
import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminUtenteUpdateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    private String password;
    private StatoUtente stato;

    @Min(0)
    private Integer eloRating;

    @Min(0)
    private Double montePremi;

    private Long[] ruoliIds;
    private Long torneoId;

    public Utente buildUtenteModel(Long id) {
        Utente model = new Utente();
        model.setId(id);
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        model.setPassword(password);
        model.setStato(stato);
        model.setEloRating(eloRating);
        model.setMontePremi(montePremi);

        if (ruoliIds != null) {
            Set<Ruolo> ruoli = Arrays.stream(ruoliIds)
                    .map(Ruolo::new)
                    .collect(Collectors.toSet());
            model.setRuoli(ruoli);
        }

        if (torneoId != null) {
            Torneo torneo = new Torneo();
            torneo.setId(torneoId);
            model.setTorneo(torneo);
        }

        return model;
    }

    public List<Ruolo> getRuoli() {
        if (ruoliIds == null) {
            return null;
        }
        return Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toList());
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

    public Long[] getRuoliIds() {
        return ruoliIds;
    }

    public void setRuoliIds(Long[] ruoliIds) {
        this.ruoliIds = ruoliIds;
    }

    public Long getTorneoId() {
        return torneoId;
    }

    public void setTorneoId(Long torneoId) {
        this.torneoId = torneoId;
    }
}
