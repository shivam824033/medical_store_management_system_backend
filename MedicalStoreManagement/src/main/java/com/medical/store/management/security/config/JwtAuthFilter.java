/**
 * 
 */
package com.medical.store.management.security.config;

import java.io.IOException;

import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.medical.store.management.services.UserDetailsServiceImpl;
import com.medical.store.management.token.Token;
//import com.medical.store.management.token.TokenRepository;
import com.medical.store.management.token.TokenDAO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Shivam jaiswal
 * 24-Aug-2024
 */

@Component
public class JwtAuthFilter extends OncePerRequestFilter  {
	
    @Autowired
    private JwtTokenUtility jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    
	@Autowired
	private TokenDAO tokenDAO;
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
          }
          final String authHeader = request.getHeader("Authorization");
          final String jwt;
          final String username;
          if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
          }
          
          try {
        	  
              jwt = authHeader.substring(7);
              username = jwtService.extractUsername(jwt);
              if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
                Token token = tokenDAO.findByToken(jwt);
                boolean isTokenValid = false;
                if(!token.isExpired() && !token.isRevoked()) {
                	isTokenValid = true;
                }
                if (jwtService.validateToken(jwt, userDetails) && isTokenValid) {
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
        	
			
		} catch (Exception e) {
			response.sendError(403, "You are not Authorized");
		}

    }


}
