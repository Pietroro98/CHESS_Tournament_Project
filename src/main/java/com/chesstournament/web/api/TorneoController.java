package com.chesstournament.web.api;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.service.torneo.TorneoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tornei")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping
    public ResponseEntity<ResponseJSON<List<TorneoDTO>>> listAll() {
        List<TorneoDTO> responseData = torneoService.listAll()
                .stream()
                .map(TorneoDTO::buildTorneoDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista tornei recuperata con successo.", responseData)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseJSON<TorneoDTO>> create(@RequestBody @Valid TorneoDTO torneoInput) {
        TorneoDTO responseData = TorneoDTO.buildTorneoDTOFromModel(
                torneoService.inserisci(torneoInput, null)
        );

        return ResponseEntity
                .status(201)
                .body(ResponseJSON.success(201, "Torneo creato con successo.", responseData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseJSON<TorneoDTO>> update(@PathVariable Long id, @RequestBody @Valid TorneoDTO torneoInput)
    {
        TorneoDTO responseData = TorneoDTO.buildTorneoDTOFromModel(
                torneoService.aggiorna(id, torneoInput, null) );

        return ResponseEntity
                .ok(ResponseJSON.success(200, "Torneo aggiornato con successo.", responseData));
    }
}
