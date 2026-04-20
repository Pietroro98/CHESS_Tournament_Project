package com.chesstournament.web.api;
import com.chesstournament.dto.AdminUtenteUpdateDTO;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.model.Utente;
import com.chesstournament.service.utente.UtenteService;
import com.chesstournament.web.api.exception.IdNotNullForInsertException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/utenti")
public class AdminUtenteController {

   private final UtenteService utenteService;

    public AdminUtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public ResponseEntity<ResponseJSON<List<UtenteDTO>>> findAll() {
        List<UtenteDTO> responseData =  utenteService.listAllUtenti()
                .stream()
                .map(UtenteDTO::buildUtenteDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista utenti recuperata con successo.", responseData)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseJSON<UtenteDTO>> findById(@PathVariable Long id) {
        Utente utente = utenteService.caricaSingoloUtente((id));
        if (utente == null) {
            return ResponseEntity.notFound().build();
        }
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utente);

        return ResponseEntity.ok(ResponseJSON.success(200, "Utente recuperato con successo.", responseData)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseJSON<UtenteDTO>> create(@RequestBody @Valid UtenteDTO utenteInput) {
        if (utenteInput.getId() != null) {
            throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
        }
        Utente utenteInserito = utenteService.inserisciNuovo(utenteInput.buildUtenteModel(false));
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utenteInserito);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseJSON.success(201, "Utente creato con successo.", responseData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseJSON<UtenteDTO>> update(@PathVariable Long id, @RequestBody @Valid AdminUtenteUpdateDTO utenteInput) {
        Utente utenteEsistente = utenteService.caricaSingoloUtente(id);
        if (utenteEsistente == null) {
            return ResponseEntity.notFound().build();
        }
        Utente utenteAggiornato = utenteService.aggiornaComeAdmin(utenteInput, id);
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Utente aggiornato con successo.", responseData)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseJSON<UtenteDTO>> disabilita(@PathVariable Long id) {
        Utente utenteEsistente = utenteService.caricaSingoloUtente(id);
        if (utenteEsistente == null) {
            return ResponseEntity.notFound().build();
        }
        utenteService.disabilita(id);
        return ResponseEntity.ok(
                ResponseJSON.success(200, "Utente disabilitato con successo.", null)
        );
    }
}
