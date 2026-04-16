package com.chesstournament.web.api.apiinfo;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Utente;
import com.chesstournament.security.dto.UtenteInfoJWTResponseDTO;
import com.chesstournament.service.utente.UtenteService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utente")
public class UserInfo {

    private final UtenteService utenteService;

    public UserInfo(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/testSoloAdmin")
    public String test() {
        return "OK";
    }

    @GetMapping("/userInfo")
    public ResponseEntity<ResponseJSON<UtenteInfoJWTResponseDTO>> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente utenteLoggato = utenteService.findByUsername(username);
        List<String> ruoli = utenteLoggato.getRuoli().stream().map(Ruolo::getCodice).toList();
        TorneoDTO torneo = utenteLoggato.getTorneo() != null
                ? TorneoDTO.buildTorneoDTOFromModel(utenteLoggato.getTorneo())
                : null;
        List<TorneoDTO> torneiCreati = utenteLoggato.getTorneiCreati().stream()
                .map(TorneoDTO::buildTorneoDTOFromModel)
                .toList();

        UtenteInfoJWTResponseDTO responseData = new UtenteInfoJWTResponseDTO(
                utenteLoggato.getNome(),
                utenteLoggato.getCognome(),
                utenteLoggato.getUsername(),
                utenteLoggato.getStato(),
                utenteLoggato.getEloRating(),
                utenteLoggato.getMontePremi(),
                torneo,
                torneiCreati,
                ruoli,
                utenteLoggato.getDataRegistrazione()
        );

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Informazioni utente recuperate con successo", responseData)
        );
    }
}
