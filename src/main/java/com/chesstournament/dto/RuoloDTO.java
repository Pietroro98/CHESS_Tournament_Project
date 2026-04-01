package com.chesstournament.dto;
import com.chesstournament.model.Ruolo;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuoloDTO {

    private Long id;
    private String descrizione;
    private String codice;

    public RuoloDTO(Long id, String descrizione, String codice) {
        this.id = id;
        this.descrizione = descrizione;
        this.codice = codice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public static RuoloDTO buildRuoloDTOFromModel(Ruolo model) {
        return new RuoloDTO(model.getId(), model.getDescrizione(), model.getCodice());
    }

    public static List<RuoloDTO> createRuoloDTOListFromModelSet(Set<Ruolo> modelList) {
        return modelList.stream().map(RuoloDTO::buildRuoloDTOFromModel).toList();
    }

    public static List<RuoloDTO> createRuoloDTOListFromModelList(List<Ruolo> modelList) {
        return modelList.stream().map(RuoloDTO::buildRuoloDTOFromModel).toList();
    }
}
