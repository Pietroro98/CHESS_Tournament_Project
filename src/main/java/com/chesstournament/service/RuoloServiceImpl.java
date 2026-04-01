package com.chesstournament.service;

import com.chesstournament.model.Ruolo;
import com.chesstournament.repository.ruolo.RuoloRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RuoloServiceImpl implements RuoloService {

    @Autowired
    private RuoloRepository ruoloRepository;

    @Override
    public List<Ruolo> listAll() {
        return (List<Ruolo>) ruoloRepository.findAll();
    }

    @Override
    public Ruolo caricaSingoloElemento(Long id) {
        return ruoloRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void aggiorna(Ruolo ruolo) {
        ruoloRepository.save(ruolo);
    }

    @Override
    @Transactional
    public void inserisciNuovo(Ruolo ruolo) {
        ruoloRepository.save(ruolo);
    }

    @Override
    @Transactional
    public void rimuovi(Long id) {
        ruoloRepository.deleteById(id);
    }

    @Override
    public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice) {
        return ruoloRepository.findByDescrizioneAndCodice(descrizione, codice);
    }
}
