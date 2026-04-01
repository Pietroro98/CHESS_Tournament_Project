package com.chesstournament.config;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.ruolo.RuoloRepository;
import com.chesstournament.repository.utente.UtenteRepository;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRuoli(RuoloRepository ruoloRepository, UtenteRepository utenteRepository,PasswordEncoder passwordEncoder)
    {
        return args -> {
            Ruolo adminRole = createRole(ruoloRepository, "Amministratore", Ruolo.ROLE_ADMIN);
            Ruolo organizerRole = createRole(ruoloRepository, "Organizzatore", Ruolo.ROLE_ORGANIZER);
            createRole(ruoloRepository, "Giocatore", Ruolo.ROLE_PLAYER);
            createUser(utenteRepository, passwordEncoder, "Admin", "System", "admin", "admin123", adminRole);
            createUser(utenteRepository, passwordEncoder, "Organizer", "System",
                "organizer", "organizer123", organizerRole);
        };
    }

    private Ruolo createRole(RuoloRepository ruoloRepository, String descrizione, String codice) {
        return ruoloRepository.findByCodice(codice)
            .orElseGet(() -> ruoloRepository.save(new Ruolo(descrizione, codice)));
    }

    private void createUser(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, String nome,
                            String cognome, String username, String rawPassword, Ruolo ruolo) {
        if (utenteRepository.existsByUsername(username)) {
            return;
        }

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setUsername(username);
        utente.setPassword(passwordEncoder.encode(rawPassword));
        utente.setDataRegistrazione(LocalDate.now());
        utente.setStato(StatoUtente.ATTIVO);
        utente.setEloRating(0);
        utente.setMontePremi(0d);
        utente.setTorneo(null);
        utente.setRuoli(Set.of(ruolo));
        utenteRepository.save(utente);
    }
}
