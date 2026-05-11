package com.fanbo.user_service.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //proba wyciagniecia jwt z naglowak zadania
            String jwt = parseJwt(request);

            // jezlei token istnieje i jest poorawny
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                //odczytanie z niego nazwy uzytkonika czyli email
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                //zaladnowanie uzytkonika z bazy, z naszego userDetailsSErviceImpl
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //utworzenie obiektu authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //od tego momentu sprig wie kim jest uzytkonik wykonujacy zadanie
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        //przkazanie zadnai dalejw lancuchu filtrow
        filterChain.doFilter(request, response);
    }

    //metoda pomocnicza do wyciagania tokenu z naglowwka "Authorization: Bearer <token>"
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
