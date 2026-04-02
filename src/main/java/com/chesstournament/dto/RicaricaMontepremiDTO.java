package com.chesstournament.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class RicaricaMontepremiDTO
{
    @NotNull
    @DecimalMin(value = "5.00", message = "L'importo minimo per la ricarica è di 5.00")
    private Double importo;

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }
}
