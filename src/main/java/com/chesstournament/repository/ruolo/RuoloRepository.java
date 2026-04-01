package com.chesstournament.repository.ruolo;
import com.chesstournament.model.Ruolo;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RuoloRepository extends CrudRepository<Ruolo, Long> {

    Ruolo findByDescrizioneAndCodice(String descrizione, String codice);

    Optional<Ruolo> findByCodice(String codice);
}
