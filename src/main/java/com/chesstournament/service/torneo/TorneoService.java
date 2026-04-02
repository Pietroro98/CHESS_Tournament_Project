package com.chesstournament.service.torneo;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.model.Torneo;

import java.util.List;

public interface TorneoService {
    List<Torneo> listAll();
    Torneo caricaDettaglioSingoloTorneo(Long id);
    Torneo inserisci(TorneoDTO dto, String username);
    Torneo aggiorna(Long id, TorneoDTO dto, String username);
    void elimina(Long id, String username);
}
