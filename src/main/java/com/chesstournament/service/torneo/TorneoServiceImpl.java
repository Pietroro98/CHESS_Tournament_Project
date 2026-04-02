package com.chesstournament.service.torneo;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.model.StatoTorneo;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.torneo.TorneoRepository;
import com.chesstournament.security.SecurityUtils;
import com.chesstournament.service.utente.UtenteService;
import com.chesstournament.web.api.exception.ForbiddenException;
import com.chesstournament.web.api.exception.NotAllowedException;
import com.chesstournament.web.api.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TorneoServiceImpl implements TorneoService {

    private final TorneoRepository torneoRepository;
    private final UtenteService utenteService;
    private final SecurityUtils securityUtils;

    public TorneoServiceImpl(TorneoRepository torneoRepository, UtenteService utenteService, SecurityUtils securityUtils) {
        this.torneoRepository = torneoRepository;
        this.utenteService = utenteService;
        this.securityUtils = securityUtils;
    }

    @Override
    public List<Torneo> listAll() {
        String username = securityUtils.getUsername();
        if (securityUtils.isOrganizer() && !securityUtils.isAdmin()) {
            return torneoRepository.findByUtenteCreazioneUsername(username);
        }

        return torneoRepository.findAll();
    }

    @Override
    public Torneo caricaDettaglioSingoloTorneo(Long id) {
        return torneoRepository.findById(id).orElse(null);
    }

    @Override
    public Torneo inserisci(TorneoDTO dto, String username) {
        String loggedUsername = securityUtils.getUsername();
        Utente utenteLoggato = utenteService.findByUsername(loggedUsername);
        if(utenteLoggato == null){
            throw new NotFoundException("Utente non trovato");
        }
        Torneo torneo = dto.buildTorneoModel(false);
        torneo.setId(null);
        torneo.setUtenteCreazione(utenteLoggato);

        if (torneo.getDataCreazione() == null) {
            torneo.setDataCreazione(LocalDate.now());
        }
        if (torneo.getStato() == null) {
            torneo.setStato(StatoTorneo.APERTO);
        }

        if(torneoRepository.existsByDenominazioneAndEloMinimoAndStatoNot(
                dto.getDenominazione(),
                dto.getEloMinimo(),
                StatoTorneo.CONCLUSO  )){
            throw new NotAllowedException("Esiste già un torneo con la stessa denominazione e elo minimo che non è concluso.");
        }

        return torneoRepository.save(torneo);
    }

    @Override
    @Transactional
    public Torneo aggiorna(Long id, TorneoDTO dto, String username) {
        String loggedUsername = securityUtils.getUsername();
        Utente utenteLoggato = utenteService.findByUsername(loggedUsername);
        if (utenteLoggato == null) {
            throw new NotFoundException("Utente non trovato");
        }
        if (id == null) {
            throw new NotFoundException("Record da aggiornare non trovato");
        }
        Torneo torneoDaAggiornare = torneoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Torneo non trovato con id: " + id));

        // validazione e sanitizzazione del owner
        validateOwner(loggedUsername, torneoDaAggiornare);

        if (!torneoDaAggiornare.getPartecipanti().isEmpty()) {
            throw new NotAllowedException("Non puoi modificare un torneo che ha gia partecipanti.");
        }

        torneoDaAggiornare.setDenominazione(dto.getDenominazione());
        torneoDaAggiornare.setDataCreazione(dto.getDataCreazione());
        torneoDaAggiornare.setStato(dto.getStato());
        torneoDaAggiornare.setEloMinimo(dto.getEloMinimo());
        torneoDaAggiornare.setQuotaIscrizione(dto.getQuotaIscrizione());
        torneoDaAggiornare.setMaxGiocatori(dto.getMaxGiocatori());

        return torneoRepository.save(torneoDaAggiornare);
    }

    @Override
    @Transactional
    public void elimina(Long id, String username) {
        String loggedUsername = securityUtils.getUsername();
        Utente utenteLoggato = utenteService.findByUsername(loggedUsername);
        if (utenteLoggato == null) {
            throw new NotFoundException("Utente non trovato");
        }
        if (id == null) {
            throw new NotFoundException("Record da eliminare non trovato");
        }

        Torneo torneoDaEliminare = torneoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Torneo non trovato con id: " + id));

        // validazione e sanitizzazione del owner
        validateOwner(loggedUsername, torneoDaEliminare);

        if (!torneoDaEliminare.getPartecipanti().isEmpty()) {
            throw new NotAllowedException("Non puoi eliminare un torneo che ha gia partecipanti.");
        }

        torneoRepository.delete(torneoDaEliminare);
    }

    /**
     * Metodo che verifica se l'utente loggato è il creatore del torneo o un admin.
     * Se non è nessuno dei due, viene lanciata un'eccezione.
     * @param loggedUsername
     * @param torneo
     */
    private void validateOwner(String loggedUsername, Torneo torneo) {
        boolean isOwner = torneo.getUtenteCreazione() != null
                && loggedUsername.equals(torneo.getUtenteCreazione().getUsername());
        if (!securityUtils.isAdmin() && !isOwner) {
            throw new ForbiddenException(loggedUsername);
        }
    }
}
