package com.chesstournament.repository.torneo;
import com.chesstournament.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByUtenteCreazioneUsername(String username);
}
