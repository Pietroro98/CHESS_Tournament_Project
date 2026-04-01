package com.chesstournament.web.api.exception;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.utente.UtenteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/utenti")
public class AdminUtenteController {

    private final UtenteRepository utenteRepository;

    public AdminUtenteController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @GetMapping
    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }
}
