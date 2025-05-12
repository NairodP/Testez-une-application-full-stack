package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;
    
    private UserDetailsImpl userDetails;
    
    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext(); // Réinitialiser le contexte entre les tests
        
        // Créer un UserDetailsImpl pour les tests
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .build();
    }
    
    @Test
    public void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // Arrange
        String validToken = "validToken";
        String authHeader = "Bearer " + validToken;
        
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(validToken)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(validToken)).thenReturn("test@test.com");
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(userDetails);
        
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(request).getHeader("Authorization");
        verify(jwtUtils).validateJwtToken(validToken);
        verify(jwtUtils).getUserNameFromJwtToken(validToken);
        verify(userDetailsService).loadUserByUsername("test@test.com");
        verify(filterChain).doFilter(request, response);
        
        // Vérifier que l'authentification a été définie
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
    
    @Test
    public void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        // Arrange
        String invalidToken = "invalidToken";
        String authHeader = "Bearer " + invalidToken;
        
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(invalidToken)).thenReturn(false);
        
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(request).getHeader("Authorization");
        verify(jwtUtils).validateJwtToken(invalidToken);
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
        
        // Vérifier que l'authentification n'a pas été définie
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testDoFilterInternalWithNoToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);
        
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(request).getHeader("Authorization");
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
        
        // Vérifier que l'authentification n'a pas été définie
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testDoFilterInternalWithInvalidAuthHeaderFormat() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat");
        
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(request).getHeader("Authorization");
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
        
        // Vérifier que l'authentification n'a pas été définie
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testDoFilterInternalHandlesException() throws ServletException, IOException {
        // Arrange
        String validToken = "validToken";
        String authHeader = "Bearer " + validToken;
        
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(validToken)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(validToken)).thenReturn("test@test.com");
        when(userDetailsService.loadUserByUsername("test@test.com")).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(request).getHeader("Authorization");
        verify(jwtUtils).validateJwtToken(validToken);
        verify(jwtUtils).getUserNameFromJwtToken(validToken);
        verify(userDetailsService).loadUserByUsername("test@test.com");
        verify(filterChain).doFilter(request, response);
        
        // Vérifier que l'authentification n'a pas été définie en cas d'exception
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
