package com.chesstournament.service;

import com.chesstournament.model.Ruolo;
import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.ruolo.RuoloRepository;
import com.chesstournament.repository.utente.UtenteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.chesstournament.web.api.exception.BadRequestException;
import com.chesstournament.web.api.exception.NotAllowedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final RuoloRepository ruoloRepository;

    public UtenteServiceImpl(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, RuoloRepository ruoloRepository) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.ruoloRepository = ruoloRepository;
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
        utenteReloaded.setNome(utenteInstance.getNome());
        utenteReloaded.setCognome(utenteInstance.getCognome());
        utenteReloaded.setUsername(utenteInstance.getUsername());

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
                        !Ruolo.ROLE_ADMIN.equals(ruolo.getCodice()) &&
                                !Ruolo.ROLE_PLAYER.equals(ruolo.getCodice())
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
                        Ruolo.ROLE_ADMIN.equals(ruolo.getCodice())
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
}
