package es.uca.wolidays.backend.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import es.uca.wolidays.backend.entities.Usuario;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
    
    @SuppressWarnings("unchecked")
	public static Collection<GrantedAuthority> roles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(authentication != null ){
        	return (Collection<GrantedAuthority>) authentication.getAuthorities();
        } else{
        	return null;
        }
    }
    
    public static String getUsername() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	return authentication.getName();
    }
    
    public static int getUserId() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Usuario user = (Usuario)authentication.getPrincipal();
    	
    	return user.getId();
    }

}