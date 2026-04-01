package com.chesstournament.web.api;
import com.chesstournament.dto.ResponseBusta;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.model.Utente;
import com.chesstournament.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/utenti")
public class AdminUtenteController {

   private final UtenteService utenteService;

    public AdminUtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public ResponseEntity<ResponseBusta<List<UtenteDTO>>> findAll() {
        List<UtenteDTO> responseData =  utenteService.listAllUtenti()
                .stream()
                .map(UtenteDTO::buildUtenteDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseBusta.success(200, "Lista utenti recuperata con successo.", responseData)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBusta<UtenteDTO>> findById(@PathVariable Long id) {
        Utente utente = utenteService.caricaSingoloUtente((id));
        if (utente == null) {
            return ResponseEntity.notFound().build();
        }
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utente);

        return ResponseEntity.ok(ResponseBusta.success(200, "Utente recuperato con successo.", responseData)
        );
    }
}
