package com.chesstournament.service.utente;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import java.util.List;

public interface UtenteService {
    List<Utente> listAllUtenti();
    Utente caricaSingoloUtente(Long id);
    Utente caricaSingoloUtenteConRuoli(Long id);
    Utente aggiorna(Utente utente, List<Ruolo> ruoliItem);
    Utente inserisciNuovo(Utente utente);
    Utente disabilita(Long id);
    Utente findByUsernameAndPassword(String username, String password);
    Utente eseguiAccesso(String username, String password);
    Utente changeUserAbilitation(Long id);
    Utente findByUsername(String username);
    Utente ricaricaMontepremi(Double importo);

    List<Torneo> ricercaTorneiCompatibili(String denominazione);
    Utente iscrivitiAlTorneo(Long idTorneo);
    ResponseJSON<UtenteDTO> giocaPartita(Long idTorneo);
}
