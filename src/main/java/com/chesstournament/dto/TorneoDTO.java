package com.chesstournament.dto;
import com.chesstournament.model.StatoTorneo;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TorneoDTO {

    private Long id;
    private String denominazione;
    private LocalDate dataCreazione;
    private StatoTorneo stato;
    private Integer eloMinimo;
    private Double quotaIscrizione;
    private Integer maxGiocatori;
    private Long utenteCreazioneId;

    public TorneoDTO() {
    }

    public Torneo buildTorneoModel(boolean includeId) {
        Torneo model = new Torneo();
        if (includeId) {
            model.setId(id);
        }
        model.setDenominazione(denominazione);
        model.setDataCreazione(dataCreazione);
        model.setStato(stato);
        model.setEloMinimo(eloMinimo);
        model.setQuotaIscrizione(quotaIscrizione);
        model.setMaxGiocatori(maxGiocatori);
        if (utenteCreazioneId != null) {
            model.setUtenteCreazione(new Utente());
            model.getUtenteCreazione().setId(utenteCreazioneId);
        }
        return model;
    }

    public static TorneoDTO buildTorneoDTOFromModel(Torneo model) {
        TorneoDTO dto = new TorneoDTO();
        dto.setId(model.getId());
        dto.setDenominazione(model.getDenominazione());
        dto.setDataCreazione(model.getDataCreazione());
        dto.setStato(model.getStato());
        dto.setEloMinimo(model.getEloMinimo());
        dto.setQuotaIscrizione(model.getQuotaIscrizione());
        dto.setMaxGiocatori(model.getMaxGiocatori());
        if (model.getUtenteCreazione() != null) {
            dto.setUtenteCreazioneId(model.getUtenteCreazione().getId());
        }
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public StatoTorneo getStato() {
        return stato;
    }

    public void setStato(StatoTorneo stato) {
        this.stato = stato;
    }

    public Integer getEloMinimo() {
        return eloMinimo;
    }

    public void setEloMinimo(Integer eloMinimo) {
        this.eloMinimo = eloMinimo;
    }

    public Double getQuotaIscrizione() {
        return quotaIscrizione;
    }

    public void setQuotaIscrizione(Double quotaIscrizione) {
        this.quotaIscrizione = quotaIscrizione;
    }

    public Integer getMaxGiocatori() {
        return maxGiocatori;
    }

    public void setMaxGiocatori(Integer maxGiocatori) {
        this.maxGiocatori = maxGiocatori;
    }

    public Long getUtenteCreazioneId() {
        return utenteCreazioneId;
    }

    public void setUtenteCreazioneId(Long utenteCreazioneId) {
        this.utenteCreazioneId = utenteCreazioneId;
    }
}
