package com.chesstournament.web.api;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.RicaricaMontepremiDTO;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.model.Utente;
import com.chesstournament.service.torneo.TorneoService;
import com.chesstournament.service.utente.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/play")
public class PlayController {

    private final TorneoService torneoService;
    private final UtenteService utenteService;

    public PlayController(TorneoService torneoService, UtenteService utenteService) {
        this.torneoService = torneoService;
        this.utenteService = utenteService;
    }

    @PostMapping("/ricarica")
    public ResponseEntity<ResponseJSON<UtenteDTO>> ricaricaMontepremi(@RequestBody @Valid RicaricaMontepremiDTO ricaricaMontepremiDTO) {

        Utente utenteAggiornato = utenteService.ricaricaMontepremi(ricaricaMontepremiDTO.getImporto());
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Montepremi ricaricato con successo.", responseData)
        );
    }

    @GetMapping("/ricerca")
    public ResponseEntity<ResponseJSON<List<TorneoDTO>>> ricercaTornei(
            @RequestParam(required = false) String denominazione) {

        List<TorneoDTO> responseData = utenteService.ricercaTorneiCompatibili(denominazione)
                .stream()
                .map(TorneoDTO::buildTorneoDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Ricerca tornei eseguita con successo.", responseData)
        );
    }


    @PostMapping("/iscriviti/{idTorneo}")
    public ResponseEntity<ResponseJSON<UtenteDTO>> iscrizioneAlTorneo(@PathVariable Long idTorneo) {
        Utente utenteAggiornato = utenteService.iscrivitiAlTorneo(idTorneo);

        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Iscrizione al torneo effettuata con successo.", responseData)
        );
    }


    @PostMapping("/gioca/{idTorneo}")
    public ResponseEntity<ResponseJSON<UtenteDTO>> giocaPartita(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(utenteService.giocaPartita(idTorneo));
    }
}
