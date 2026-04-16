package com.chesstournament.security.auth;

import com.chesstournament.dto.ResponseJSON;
import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.ruolo.RuoloRepository;
import com.chesstournament.repository.utente.UtenteRepository;
import com.chesstournament.security.JWTUtil;
import com.chesstournament.security.dto.UsernameCheckResponseDTO;
import com.chesstournament.security.dto.UsernameRegisterCheckDTO;
import com.chesstournament.security.dto.UtenteAuthJWTResponseDTO;
import com.chesstournament.security.dto.UtenteAuthLoginDTO;
import com.chesstournament.security.dto.UtenteAuthRegisterDTO;
import com.chesstournament.service.utente.UtenteService;
import com.chesstournament.web.api.exception.BadRequestException;
import jakarta.validation.Valid;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UtenteService utenteService;
    private final UtenteRepository utenteRepository;
    private final RuoloRepository ruoloRepository;

    public AuthController(JWTUtil jwtUtil, AuthenticationManager authManager, UtenteService utenteService,
                          UtenteRepository utenteRepository, RuoloRepository ruoloRepository) {
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.utenteService = utenteService;
        this.utenteRepository = utenteRepository;
        this.ruoloRepository = ruoloRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseJSON<UtenteAuthJWTResponseDTO>> loginHandler(@RequestBody @Valid UtenteAuthLoginDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authManager.authenticate(authInputToken);

            Utente utente = utenteService.findByUsername(body.getUsername());
            List<String> roles = utente.getRuoli().stream().map(Ruolo::getCodice).toList();

            String token = jwtUtil.generateToken(body.getUsername());
            UtenteAuthJWTResponseDTO responseData =
                    new UtenteAuthJWTResponseDTO(token, body.getUsername(), roles);

            return ResponseEntity.ok(
                    ResponseJSON.success(200, "Login effettuato con successo", responseData)
            );
        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login Credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteAuthJWTResponseDTO> register(@RequestBody @Valid UtenteAuthRegisterDTO body) {
        if (body.getId() != null)
        {
            throw new BadRequestException("Attenzione, l'id non deve essere valorizzato in inserimento");
        }

        Ruolo defaultRole = ruoloRepository.findByCodice(Ruolo.ROLE_PLAYER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ruolo PLAYER non configurato"));

        Utente entity = body.buildUtenteModel();
        entity.setRuoli(Set.of(defaultRole));
        utenteService.inserisciNuovo(entity);

        String token = jwtUtil.generateToken(entity.getUsername());
        List<String> roles = entity.getRuoli().stream().map(Ruolo::getCodice).toList();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new UtenteAuthJWTResponseDTO(token, entity.getUsername(), roles));
    }

    @PostMapping("/check-username")
    public ResponseEntity<ResponseJSON<UsernameCheckResponseDTO>> usernameCheck(@RequestBody @Valid UsernameRegisterCheckDTO body) {
        boolean exists = utenteRepository.existsByUsername(body.getUsername());

        if (exists) {
            UsernameCheckResponseDTO responseData =
                    new UsernameCheckResponseDTO(false, buildUsernameSuggeriti(body.getUsername()));

            return ResponseEntity.ok(
                    ResponseJSON.success(200, "Username non disponibile", responseData)
            );
        }

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Username disponibile", new UsernameCheckResponseDTO(true, List.of()))
        );
    }

    private List<String> buildUsernameSuggeriti(String username) {
        // Normalizza il valore in ingresso ed evita null o stringhe vuote.
        String normalized = username == null ? "" : username.trim();
        if (normalized.isBlank()) {
            normalized = "player";
        }

        // Mantiene solo lettere, numeri e underscore per costruire username validi.
        String base = normalized.replaceAll("[^A-Za-z0-9_]", "");
        if (base.isBlank()) {
            base = "player";
        }

        // Rimuove gli underscore finali per evitare risultati tipo "Pietroro__2026".
        base = base.replaceAll("_+$", "");
        if (base.isBlank()) {
            base = "player";
        }

        int currentYear = Year.now().getValue();
        // LinkedHashSet preserva l'ordine ed evita suggerimenti duplicati.
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        candidates.add(base + "1");
        candidates.add(base + "10");
        candidates.add(base + "123");
        candidates.add(base + "_" + currentYear);
        candidates.add(base + "_" + (currentYear + 1));
        candidates.add(base + "_01");
        candidates.add(base + "_99");
        candidates.add(base + "Chess");
        candidates.add("real_" + base);
        candidates.add(base + "_official");

        List<String> availableSuggestions = new ArrayList<>();
        for (String candidate : candidates) {
            // Tiene solo i candidati non ancora presenti nel database.
            if (!utenteRepository.existsByUsername(candidate)) {
                availableSuggestions.add(candidate);
            }

            // Limita la risposta a 3 suggerimenti per non appesantire il payload.
            if (availableSuggestions.size() == 3) {
                break;
            }
        }

        return availableSuggestions;
    }
}
