package com.chesstournament.web.api;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.RicaricaMontepremiDTO;
import com.chesstournament.dto.TorneoDTO;
import com.chesstournament.model.Utente;
import com.chesstournament.service.torneo.TorneoService;
import com.chesstournament.service.utente.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseJSON<List<TorneoDTO>>> ricaricaMontepremi(@RequestBody @Valid RicaricaMontepremiDTO ricaricaMontepremiDTO) {
        Utente aggiornaMontepremi = utenteService.ricaricaMontepremi(ricaricaMontepremiDTO.getImporto());

        return new ResponseEntity<>(new ResponseJSON<List<TorneoDTO>>(), HttpStatus.OK);
    }

}
