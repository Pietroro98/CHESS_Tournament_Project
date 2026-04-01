package com.chesstournament.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class RicaricaMontepremiDTO
{
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double importo;

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }
}
