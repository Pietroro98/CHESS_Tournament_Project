package com.chesstournament.service.utente;

import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Torneo;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.ruolo.RuoloRepository;
import com.chesstournament.repository.torneo.TorneoRepository;
import com.chesstournament.repository.utente.UtenteRepository;
import com.chesstournament.security.SecurityUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.chesstournament.service.torneo.TorneoService;
import com.chesstournament.web.api.exception.BadRequestException;
import com.chesstournament.web.api.exception.ForbiddenException;
import com.chesstournament.web.api.exception.NotAllowedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final RuoloRepository ruoloRepository;
    private final SecurityUtils securityUtils;
    private final TorneoRepository torneoRepository;

    public UtenteServiceImpl(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder,
                             RuoloRepository ruoloRepository, SecurityUtils securityUtils, TorneoRepository torneoRepository) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.ruoloRepository = ruoloRepository;
        this.securityUtils = securityUtils;
        this.torneoRepository = torneoRepository;
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
    public Utente aggiorna(Utente utenteInstance, List<Ruolo> ruoliItem) {
        Utente utenteReloaded = utenteRepository.findById(utenteInstance.getId()).orElse(null);
        if (utenteReloaded == null) {
            throw new RuntimeException("Elemento non trovato");
        }

        boolean isAdmin = securityUtils.isAdmin();
        String usernameLoggato = securityUtils.getUsername();

        if (!isAdmin && !usernameLoggato.equals(utenteReloaded.getUsername())) {
            throw new ForbiddenException(usernameLoggato);
        }

        utenteReloaded.setNome(utenteInstance.getNome());
        utenteReloaded.setCognome(utenteInstance.getCognome());
        utenteReloaded.setUsername(utenteInstance.getUsername());

        if (!isAdmin) {
            return utenteRepository.save(utenteReloaded);
        }

        if (ruoliItem == null || ruoliItem.isEmpty()) {
            throw new BadRequestException("Devi specificare almeno un ruolo");
        }

        Set<Ruolo> ruoliValidi = ruoliItem
                .stream()
                .map(ruoloInput -> {
                    if (ruoloInput.getId() == null) {
                        throw new BadRequestException("Ogni ruolo deve avere un id");
                    }

                    return ruoloRepository.findById(ruoloInput.getId())
                            .orElseThrow(() -> new BadRequestException(
                                    "Ruolo non valido con id: " + ruoloInput.getId()));
                }).collect(Collectors.toSet());

        boolean contieneAdmin = ruoliValidi.stream()
                .anyMatch(ruolo ->
                        Ruolo.ROLE_ADMIN.equals(ruolo.getCodice())
                );

        if (contieneAdmin) {
            throw new NotAllowedException("Non è consentito assegnare il ruolo ADMIN");
        }

        utenteReloaded.setRuoli(ruoliValidi);

        return utenteRepository.save(utenteReloaded);
    }

    @Override
    public Utente inserisciNuovo(Utente entity)
    {
        entity.setStato(StatoUtente.ATTIVO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDataRegistrazione(LocalDate.now());
        entity.setEloRating(0);
        entity.setMontePremi(0d);
        entity.setTorneo(null);

        if (entity.getRuoli() == null || entity.getRuoli().isEmpty()) {
            throw new RuntimeException("L'utente deve avere almeno un ruolo.");
        }

        Set<Ruolo> ruoliValidi = entity.getRuoli().stream()
                .map(ruoloInput -> ruoloRepository.findById(ruoloInput.getId())
                        .orElseThrow(() -> new BadRequestException(
                                "Ruolo non valido con id: " + ruoloInput.getId())))
                .collect(Collectors.toSet());

        boolean contieneRuoloNonConsentito = ruoliValidi.stream()
                .anyMatch(ruolo ->
                        !Ruolo.ROLE_ADMIN.equals(ruolo.getCodice()) &&
                                !Ruolo.ROLE_PLAYER.equals(ruolo.getCodice())
                );

        if (contieneRuoloNonConsentito) {
            throw new NotAllowedException("L'admin può creare solo utenti con ruolo PLAYER o ORGANIZER");
        }

        entity.setRuoli(ruoliValidi);

        return utenteRepository.save(entity);
    }

    @Override
    public Utente disabilita(Long id) {
        Utente entity = caricaSingoloUtente(id);
        if (entity == null) {
            throw new RuntimeException("Elemento non trovato.");
        }
        entity.setStato(StatoUtente.DISABILITATO);
        return utenteRepository.save(entity);
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
    public Utente changeUserAbilitation(Long utenteInstanceId) {
        Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
        if (utenteInstance == null) {
            throw new RuntimeException("Elemento non trovato.");
        }
        if (StatoUtente.ATTIVO.equals(utenteInstance.getStato())) {
            utenteInstance.setStato(StatoUtente.DISABILITATO);
        } else if (StatoUtente.DISABILITATO.equals(utenteInstance.getStato())) {
            utenteInstance.setStato(StatoUtente.ATTIVO);
        }
        return utenteRepository.save(utenteInstance);
    }

    @Override
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public Utente ricaricaMontepremi(Double importo) {
        String username = securityUtils.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente autenticato non trovato."));

        double montePremiAttuale = utente.getMontePremi() != null ? utente.getMontePremi() : 0d;
        utente.setMontePremi(montePremiAttuale + importo);

        return utenteRepository.save(utente);
    }

    @Override
    public Utente iscrivitiAlTorneo(Long idTorneo) {
        String username = securityUtils.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente autenticato non trovato."));

        if (utente.getTorneo() != null) {
            throw new NotAllowedException("L'utente è già iscritto a un torneo.");
        }

        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new RuntimeException("Torneo non trovato."));

        Double montePremi = utente.getMontePremi() != null ? utente.getMontePremi() : 0d;
        Double quotaIscrizione = torneo.getQuotaIscrizione() != null ? torneo.getQuotaIscrizione() : 0d;

        if(montePremi < quotaIscrizione) {
            throw new NotAllowedException("Non hai abbastanza credito per iscriverti a questo torneo.");
        }
        Integer eloUtente = utente.getEloRating() != null ? utente.getEloRating() : 0;
        Integer eloMinimo = torneo.getEloMinimo() != null ? torneo.getEloMinimo() : 0;

        if (eloUtente < eloMinimo) {
            throw new BadRequestException("Elo rating insufficiente per iscriversi al torneo.");
        }
        utente.setMontePremi(montePremi - quotaIscrizione);
        utente.setTorneo(torneo);

        return utenteRepository.save(utente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Torneo> ricercaTorneiCompatibili(String denominazione) {
        String username = securityUtils.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente autenticato non trovato."));

        Integer eloUtente = utente.getEloRating() != null ? utente.getEloRating() : 0;

        List<Torneo> tornei = torneoRepository.findAll();

        return tornei.stream()
                .filter(torneo -> {
                    Integer eloMinimo = torneo.getEloMinimo() != null ? torneo.getEloMinimo() : 0;
                    return eloMinimo <= eloUtente;
                })
                .filter(torneo ->
                        denominazione == null || denominazione.isBlank() ||
                                torneo.getDenominazione().toLowerCase().contains(denominazione.toLowerCase())
                )
                .toList();
    }

    @Override
    @Transactional
    public ResponseJSON<UtenteDTO> giocaPartita(Long idTorneo) {
        String username = securityUtils.getUsername();
        Utente utente = utenteRepository.findByUsernameConTorneoEPartecipanti(username)
                .orElseThrow(() -> new RuntimeException("Utente autenticato non trovato."));

        if (utente.getTorneo() == null) {
            throw new BadRequestException("Il giocatore non è iscritto ad alcun torneo.");
        }

        Torneo torneo = utente.getTorneo();

        if (!torneo.getId().equals(idTorneo)) {
            throw new BadRequestException("Il torneo indicato non coincide con quello in cui il giocatore è iscritto.");
        }

        if (torneo.getPartecipanti() == null || torneo.getPartecipanti().size() < 2) {
            throw new BadRequestException("Non è possibile giocare una partita se non ci sono almeno 2 partecipanti al torneo.");
        }

        String messaggio = simulaPartita(utente);

        Utente salvato = utenteRepository.save(utente);
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(salvato);

        return ResponseJSON.success(200, messaggio, responseData);
    }


    private String simulaPartita(Utente utente) {
        double esito = Math.random();
        int somma = (int) (Math.random() * 500);
        int delta;
        // 0.00 - 0.33 => sconfitta
        // 0.33 - 0.66 => patta
        // 0.66 - 1.00 => vittoria
        if (esito < 0.33) {
            delta = -somma;
        } else if (esito < 0.66) {
            delta = 0;
        } else {
            delta = somma;
        }

        double montePremiAttuale = utente.getMontePremi() != null ? utente.getMontePremi() : 0d;
        double nuovoMontepremi = montePremiAttuale + delta;

        String messaggio = "Partita simulata con successo.";

        if (nuovoMontepremi < 0) {
            nuovoMontepremi = 0;
            messaggio = "credito esaurito";
        }

        int eloAttuale = utente.getEloRating() != null ? utente.getEloRating() : 0;

        utente.setMontePremi(nuovoMontepremi);
        utente.setEloRating(eloAttuale + 5); // incremento fisso ad ogni partita

        return messaggio;
    }
}
