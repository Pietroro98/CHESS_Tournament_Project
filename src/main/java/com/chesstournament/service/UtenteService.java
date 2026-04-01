package com.chesstournament.service;

import com.chesstournament.model.Utente;
import java.util.List;

public interface UtenteService {
    List<Utente> listAllUtenti();
    Utente caricaSingoloUtente(Long id);
    Utente caricaSingoloUtenteConRuoli(Long id);
    void aggiorna(Utente utente);
    void inserisciNuovo(Utente utente);
    void disabilita(Long id);
    Utente findByUsernameAndPassword(String username, String password);
    Utente eseguiAccesso(String username, String password);
    void changeUserAbilitation(Long id);
    Utente findByUsername(String username);
}
