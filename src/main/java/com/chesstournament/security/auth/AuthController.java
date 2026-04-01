package com.chesstournament.security.auth;

import com.chesstournament.model.Ruolo;
import com.chesstournament.model.Utente;
import com.chesstournament.repository.ruolo.RuoloRepository;
import com.chesstournament.repository.utente.UtenteRepository;
import com.chesstournament.security.JWTUtil;
import com.chesstournament.security.dto.UtenteAuthJWTResponseDTO;
import com.chesstournament.security.dto.UtenteAuthLoginDTO;
import com.chesstournament.security.dto.UtenteAuthRegisterDTO;
import com.chesstournament.service.utente.UtenteService;
import com.chesstournament.web.api.exception.BadRequestException;
import com.chesstournament.web.api.exception.NotAllowedException;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public Map<String, Object> loginHandler(@RequestBody @Valid UtenteAuthLoginDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(body.getUsername());
            return Collections.singletonMap("jwt-token", token);
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

        if (utenteRepository.existsByUsername(body.getUsername())) {
            throw new NotAllowedException("Attenzione, username già esistente");
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
}
