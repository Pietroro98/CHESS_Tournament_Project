package com.chesstournament.config;
import com.chesstournament.model.Ruolo;
import com.chesstournament.repository.ruolo.RuoloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRuoli(RuoloRepository ruoloRepository) {
        return args -> {
            ensureRole(ruoloRepository, "Amministratore", Ruolo.ROLE_ADMIN);
            ensureRole(ruoloRepository, "Organizzatore", Ruolo.ROLE_ORGANIZER);
            ensureRole(ruoloRepository, "Giocatore", Ruolo.ROLE_PLAYER);
        };
    }

    private void ensureRole(RuoloRepository ruoloRepository, String descrizione, String codice) {
        ruoloRepository.findByCodice(codice).orElseGet(() -> ruoloRepository.save(new Ruolo(descrizione, codice)));
    }
}
