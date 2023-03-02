package it.projectactivitymanager.PAT.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // Richiesta HTTP - Possiamo intercettarla e estrarre dati
            @NonNull HttpServletResponse response, // Risposta HTTP - Possiamo intercettarla e immettere nuovi dati (es. header)
            @NonNull FilterChain filterChain // chain of responsability -- list of other filter
            /*
            Non possono essere NULL
             */
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); //Prendiamo il token JWT
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);  //Bearer_ lungo 7! prendiamo toker cosi

        /* Controlliamo che User sia nel database, però prima estriamo username */
        userEmail = jwtService.extractUsername(jwt); // todo extract the userMail from JWT Token;
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // se è null getAuthentication significa che non si era ancora autenticato
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
    /*
    Ogni richiesta HTTP deve attivare il filtro --> OncePerRequestFilter
     */
}
