package com.chesstournament.repository.ruolo;
import com.chesstournament.model.Ruolo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {

    Ruolo findByDescrizioneAndCodice(String descrizione, String codice);

    Optional<Ruolo> findByCodice(String codice);
}
