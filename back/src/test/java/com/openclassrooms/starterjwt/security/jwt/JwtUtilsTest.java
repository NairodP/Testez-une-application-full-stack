package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        // Configuration de la clé secrète et de l'expiration pour les tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1000); // 1 seconde pour faciliter le test d'expiration
        
        // Création d'un UserDetailsImpl pour les tests
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser@example.com")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .build();
    }

    @Test
    public void testGenerateJwtToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        // Act
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(authentication).getPrincipal();
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);
        
        // Assert
        assertEquals("testuser@example.com", username);
    }

    @Test
    public void testValidateJwtToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);
        
        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtTokenWithInvalidToken() {
        // Act
        boolean isValid = jwtUtils.validateJwtToken("invalidToken");
        
        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testValidateExpiredJwtToken() throws InterruptedException {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Attendre que le token expire (configuré à 1 seconde)
        Thread.sleep(1100);
        
        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);
        
        // Assert
        assertFalse(isValid);
    }
}
