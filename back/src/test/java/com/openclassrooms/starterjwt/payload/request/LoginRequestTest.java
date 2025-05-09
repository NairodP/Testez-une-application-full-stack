package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    public void testSetAndGetEmail() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String email = "test@test.com";
        
        // Act
        loginRequest.setEmail(email);
        
        // Assert
        assertEquals(email, loginRequest.getEmail());
    }
    
    @Test
    public void testSetAndGetPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String password = "password123";
        
        // Act
        loginRequest.setPassword(password);
        
        // Assert
        assertEquals(password, loginRequest.getPassword());
    }
    
    @Test
    public void testDefaultValuesAreNull() {
        // Arrange & Act
        LoginRequest loginRequest = new LoginRequest();
        
        // Assert
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }
}