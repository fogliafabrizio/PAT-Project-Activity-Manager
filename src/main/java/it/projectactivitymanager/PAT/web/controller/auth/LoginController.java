package it.projectactivitymanager.PAT.web.controller.auth;

import it.projectactivitymanager.PAT.web.request.AuthenticationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @ModelAttribute("authRequest")
    public AuthenticationRequest getAuthenticationRequest() {
        return new AuthenticationRequest();
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authRequest");
        return "login";
    }
}

