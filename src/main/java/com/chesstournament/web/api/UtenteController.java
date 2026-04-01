package com.chesstournament.web.api;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Utente;
import com.chesstournament.security.dto.UtenteInfoJWTResponseDTO;
import com.chesstournament.service.UtenteService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utente")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/testSoloAdmin")
    public String test() {
        return "OK";
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UtenteInfoJWTResponseDTO> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente utenteLoggato = utenteService.findByUsername(username);
        List<String> ruoli = utenteLoggato.getRuoli().stream().map(Ruolo::getCodice).toList();

        return ResponseEntity.ok(new UtenteInfoJWTResponseDTO(
            utenteLoggato.getNome(),
            utenteLoggato.getCognome(),
            utenteLoggato.getUsername(),
            ruoli
        ));
    }
}
