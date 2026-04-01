package com.chesstournament.service.ruolo;

import com.chesstournament.model.Ruolo;
import java.util.List;

public interface RuoloService {
    List<Ruolo> listAll();
    Ruolo caricaSingoloElemento(Long id);
    void aggiorna(Ruolo ruolo);
    void inserisciNuovo(Ruolo ruolo);
    void rimuovi(Long id);
    Ruolo cercaPerDescrizioneECodice(String descrizione, String codice);
}
