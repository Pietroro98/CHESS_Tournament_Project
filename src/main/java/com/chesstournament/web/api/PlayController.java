package com.chesstournament.web.api;
import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.dto.RicaricaMontepremiDTO;
import com.chesstournament.dto.UtenteDTO;
import com.chesstournament.model.Utente;
import com.chesstournament.service.torneo.TorneoService;
import com.chesstournament.service.utente.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
