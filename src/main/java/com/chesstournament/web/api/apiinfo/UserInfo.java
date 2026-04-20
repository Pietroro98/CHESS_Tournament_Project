package com.chesstournament.web.api.apiinfo;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.dto.UtenteUpdateDTO;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Utente;
import com.chesstournament.security.dto.ChangePasswordDTO;
import com.chesstournament.security.dto.UtenteInfoJWTResponseDTO;
import com.chesstournament.service.utente.UtenteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseJSON<Void>> changePassword(@RequestBody @Valid ChangePasswordDTO body) {
        utenteService.changePassword(body.getCurrentPassword(), body.getNewPassword(), body.getConfirmPassword());

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Password modificata con successo.", null)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ResponseJSON<UtenteDTO>> updateMe(@RequestBody @Valid UtenteUpdateDTO body) {
        Utente utenteAggiornato = utenteService.aggiornaProfilo(body);
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Profilo aggiornato con successo.", responseData)
        );
    }
}
