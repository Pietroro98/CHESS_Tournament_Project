package com.chesstournament.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "torneo")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "denominazione")
    private String denominazione;

    @Column(name = "dataCreazione")
    private LocalDate dataCreazione;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato")
    private StatoTorneo stato;

    @Column(name = "eloMinimo")
    private Integer eloMinimo;

    @Column(name = "quotaIscrizione")
    private Double quotaIscrizione;

    @Column(name = "maxGiocatori")
    private Integer maxGiocatori;

    @OneToMany(mappedBy = "torneo")
    private Set<Utente> partecipanti = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_creazione_id")
    private Utente utenteCreazione;

    public Torneo() {
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

    public Set<Utente> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(Set<Utente> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public Utente getUtenteCreazione() {
        return utenteCreazione;
    }

    public void setUtenteCreazione(Utente utenteCreazione) {
        this.utenteCreazione = utenteCreazione;
    }
}
