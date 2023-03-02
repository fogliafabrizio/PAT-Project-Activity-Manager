package it.projectactivitymanager.PAT.web.controller.auth;

import it.projectactivitymanager.PAT.config.security.AuthenticationService;
import it.projectactivitymanager.PAT.web.request.AuthenticationRequest;
import it.projectactivitymanager.PAT.web.request.RegisterRequest;
import it.projectactivitymanager.PAT.web.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public String auth2(
            @ModelAttribute AuthenticationRequest request
    ){
        AuthenticationResponse authenticationResponse = service.authenticate(request);
        if(authenticationResponse.getToken() != null) {
            // todo settare Token all'utente
            return "demo";
        } else {
            return "login";
        }
    }
}
