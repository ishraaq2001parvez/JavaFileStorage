package com.iparvez.fileapi.demo.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iparvez.fileapi.demo.services.UserService;
import com.iparvez.fileapi.demo.services.jwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtFilter extends OncePerRequestFilter{

    @Autowired jwtService jwtService; 

    @Autowired ApplicationContext context; 

    /*
     * function to add before userauth filter is run
     * verifies the jwt token and checks whether it matches with correct uname and pw
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*
         * take the authentication header from the request
         * we will extract the username and the token from there
         */
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        /*
         * if the token recieved is valid
         * split by predefined rules
         */
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            userName = jwtService.extractUserName(token);
        }

        /*
         * check if the username retrieved is correct
         * and if no user is logged in
         */
        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null){

            /*
             * we need to check if there is a user by the retrieved username
             */
            UserDetails userDetails = context.getBean(UserService.class).loadUserByUsername(userName);

            /*
             * run the validate function
             */
            if(jwtService.validateToken(token, userDetails)){
                /*
                 * create a new unamepwauthtoken object
                 * this is the one spring security actually uses to work with
                 * we set the principal as the user, the details as null, since jwt verified already
                 * and we set the authorities as the current user authorities
                 */
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                /*
                 * create a new webauthdetails object
                 * this tracks crucial data, like when and where the user logged in
                 */
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                /*
                 * put the obtained user into the security context
                 * authentication done!
                 */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
        
    }
}
