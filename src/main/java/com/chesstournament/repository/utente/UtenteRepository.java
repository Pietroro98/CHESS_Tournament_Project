package com.chesstournament.repository.utente;

import com.chesstournament.model.StatoUtente;
import com.chesstournament.model.Utente;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

    @EntityGraph(attributePaths = "ruoli")
    Optional<Utente> findByUsername(String username);

    @Query("from Utente u left join fetch u.ruoli where u.id = ?1")
    Optional<Utente> findByIdConRuoli(Long id);

    Utente findByUsernameAndPassword(String username, String password);

    @EntityGraph(attributePaths = "ruoli")
    Utente findByUsernameAndPasswordAndStato(String username, String password, StatoUtente stato);

    boolean existsByUsername(String username);
}
