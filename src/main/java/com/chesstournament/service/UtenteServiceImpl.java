package com.chesstournament.service;

import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.utente.UtenteRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteServiceImpl(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Utente> listAllUtenti() {
        return (List<Utente>) utenteRepository.findAll();
    }

    @Override
    public Utente caricaSingoloUtente(Long id) {
        return utenteRepository.findById(id).orElse(null);
    }

    @Override
    public Utente caricaSingoloUtenteConRuoli(Long id) {
        return utenteRepository.findByIdConRuoli(id).orElse(null);
    }

    @Override
    @Transactional
    public void aggiorna(Utente utenteInstance) {
        Utente utenteReloaded = utenteRepository.findById(utenteInstance.getId()).orElse(null);
        if (utenteReloaded == null) {
            throw new RuntimeException("Elemento non trovato");
        }
        utenteReloaded.setNome(utenteInstance.getNome());
        utenteReloaded.setCognome(utenteInstance.getCognome());
        utenteReloaded.setUsername(utenteInstance.getUsername());
        utenteReloaded.setRuoli(utenteInstance.getRuoli());
        utenteRepository.save(utenteReloaded);
    }

    @Override
    public void inserisciNuovo(Utente entity) {
        entity.setStato(StatoUtente.ATTIVO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDataRegistrazione(LocalDate.now());
        entity.setEloRating(0);
        entity.setMontePremi(0d);
        entity.setTorneo(null);
        utenteRepository.save(entity);
    }

    @Override
    public void disabilita(Long id) {
        Utente entity = caricaSingoloUtente(id);
        if (entity == null) {
            throw new RuntimeException("Elemento non trovato.");
        }
        entity.setStato(StatoUtente.DISABILITATO);
        utenteRepository.save(entity);
    }

    @Override
    public Utente eseguiAccesso(String username, String password) {
        return utenteRepository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
    }

    @Override
    public Utente findByUsernameAndPassword(String username, String password) {
        return utenteRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    @Transactional
    public void changeUserAbilitation(Long utenteInstanceId) {
        Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
        if (utenteInstance == null) {
            throw new RuntimeException("Elemento non trovato.");
        }
        if (StatoUtente.ATTIVO.equals(utenteInstance.getStato())) {
            utenteInstance.setStato(StatoUtente.DISABILITATO);
        } else if (StatoUtente.DISABILITATO.equals(utenteInstance.getStato())) {
            utenteInstance.setStato(StatoUtente.ATTIVO);
        }
    }

    @Override
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username).orElse(null);
    }
}
