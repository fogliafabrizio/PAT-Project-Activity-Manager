package it.projectactivitymanager.PAT.web.controller.auth;

import io.micrometer.common.util.StringUtils;
import it.projectactivitymanager.PAT.config.security.AuthenticationService;
import it.projectactivitymanager.PAT.web.request.AuthenticationRequest;
import it.projectactivitymanager.PAT.web.request.RegisterRequest;
import it.projectactivitymanager.PAT.web.response.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> auth(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/auth2")
    public String auth2(@ModelAttribute("authRequest") AuthenticationRequest request, Model model, HttpServletResponse response) {
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());
        AuthenticationResponse authenticationResponse = service.authenticate(request);
        String jwtToken = authenticationResponse.getToken();

        // Controlla se il token è null o vuoto
            if (StringUtils.isEmpty(jwtToken)) {
                // Se è null o vuoto, l'autenticazione non è andata a buon fine, rimanda l'utente alla pagina di login
                return "redirect:/login?error";
            }

        // Aggiungi il token nell'header della risposta
                response.setHeader("Authorization", "Bearer " + jwtToken);

        // rimanda l'utente alla pagina demo.html
                return "demo";
    }

}
