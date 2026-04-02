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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "dataRegistrazione")
    private LocalDate dataRegistrazione;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato")
    private StatoUtente stato;

    @Column(name = "eloRating")
    private Integer eloRating = 0;

    @Column(name = "montePremi")
    private Double montePremi;

    @ManyToMany
    @JoinTable(
        name = "utente_ruolo",
        joinColumns = @JoinColumn(name = "utente_id"),
        inverseJoinColumns = @JoinColumn(name = "ruolo_id")
    )
    private Set<Ruolo> ruoli = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    @OneToMany(mappedBy = "utenteCreazione")
    private Set<Torneo> torneiCreati = new HashSet<>();

    public Utente() {
    }

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Utente(String username, String password, String nome, String cognome, LocalDate dateCreated) {
        this(username, password);
        this.nome = nome;
        this.cognome = cognome;
        this.dataRegistrazione = dateCreated;
    }

    public Utente(String username, String password, String nome, String cognome, LocalDate dateCreated,
                  Integer eloRating, Double montePremi) {
        this(username, password, nome, cognome, dateCreated);
        this.eloRating = eloRating;
        this.montePremi = montePremi;
    }

    public Utente(Long id, String nome, String cognome, String username, String password,
                  LocalDate dataRegistrazione, StatoUtente stato, Integer eloRating, Double montePremi) {
        this(username, password, nome, cognome, dataRegistrazione, eloRating, montePremi);
        this.id = id;
        this.stato = stato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(Set<Ruolo> ruoli) {
        this.ruoli = ruoli;
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

    public LocalDate getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(LocalDate dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public StatoUtente getStato() {
        return stato;
    }

    public void setStato(StatoUtente stato) {
        this.stato = stato;
    }

    public boolean isAdmin() {
        return ruoli.stream().anyMatch(item -> Ruolo.ROLE_ADMIN.equals(item.getCodice()));
    }

    public boolean isAttivo() {
        return stato != null && stato.equals(StatoUtente.ATTIVO);
    }

    public boolean isDisabilitato() {
        return stato != null && stato.equals(StatoUtente.DISABILITATO);
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

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Set<Torneo> getTorneiCreati() {
        return torneiCreati;
    }

    public void setTorneiCreati(Set<Torneo> torneiCreati) {
        this.torneiCreati = torneiCreati;
    }
}
