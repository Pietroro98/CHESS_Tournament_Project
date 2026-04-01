package com.chesstournament.service.torneo;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.model.StatoTorneo;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.torneo.TorneoRepository;
import com.chesstournament.security.SecurityUtils;
import com.chesstournament.service.UtenteService;
import com.chesstournament.web.api.exception.NotFoundException;
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
    public Torneo caricaDettaglioSingoloTorneo(Long id, String username) {
        throw new NotFoundException("nd");
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

        return torneoRepository.save(torneo);
    }

    @Override
    public Torneo aggiorna(Long id, TorneoDTO dto, String username) {
        throw new NotFoundException("nd");
    }

    @Override
    public void elimina(Long id, String username) {
        throw new NotFoundException("Nd");
    }
}
