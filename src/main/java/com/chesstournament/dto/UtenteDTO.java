package com.chesstournament.dto;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtenteDTO {

    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private LocalDate dataRegistrazione;
    private StatoUtente stato;
    private Integer eloRating;
    private Double montePremi;
    private List<RuoloDTO> ruolo;
    private Long[] ruoliIds;
    private Long torneoId;

    public UtenteDTO() {
    }

    public UtenteDTO(Long id, String nome, String cognome, String username, StatoUtente stato) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.stato = stato;
    }

    public Utente buildUtenteModel(boolean includeId) {
        Utente model = new Utente();
        if (includeId) {
            model.setId(id);
        }
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        model.setPassword(password);
        model.setDataRegistrazione(dataRegistrazione);
        model.setStato(stato);
        model.setEloRating(eloRating);
        model.setMontePremi(montePremi);
        if (ruoliIds != null) {
            Set<Ruolo> ruoli = Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toSet());
            model.setRuoli(ruoli);
        }
        if (torneoId != null) {
            Torneo torneo = new Torneo();
            torneo.setId(torneoId);
            model.setTorneo(torneo);
        }
        return model;
    }

    public static UtenteDTO buildUtenteDTOFromModel(Utente model) {
        UtenteDTO dto = new UtenteDTO(model.getId(), model.getNome(), model.getCognome(), model.getUsername(), model.getStato());
        dto.setDataRegistrazione(model.getDataRegistrazione());
        dto.setEloRating(model.getEloRating());
        dto.setMontePremi(model.getMontePremi());
        dto.setRuolo(RuoloDTO.createRuoloDTOListFromModelSet(model.getRuoli()));
        if (model.getTorneo() != null) {
            dto.setTorneoId(model.getTorneo().getId());
        }
        return dto;
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

    public List<RuoloDTO> getRuolo() {
        return ruolo;
    }

    public void setRuolo(List<RuoloDTO> ruolo) {
        this.ruolo = ruolo;
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
