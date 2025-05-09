package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    @Test
    public void testEmailGetterAndSetter() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        String email = "test@test.com";
        
        // Act
        signupRequest.setEmail(email);
        
        // Assert
        assertEquals(email, signupRequest.getEmail());
    }
    
    @Test
    public void testFirstNameGetterAndSetter() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        String firstName = "John";
        
        // Act
        signupRequest.setFirstName(firstName);
        
        // Assert
        assertEquals(firstName, signupRequest.getFirstName());
    }
    
    @Test
    public void testLastNameGetterAndSetter() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        String lastName = "Doe";
        
        // Act
        signupRequest.setLastName(lastName);
        
        // Assert
        assertEquals(lastName, signupRequest.getLastName());
    }
    
    @Test
    public void testPasswordGetterAndSetter() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        String password = "password123";
        
        // Act
        signupRequest.setPassword(password);
        
        // Assert
        assertEquals(password, signupRequest.getPassword());
    }
    
    @Test
    public void testDefaultValuesAreNull() {
        // Arrange & Act
        SignupRequest signupRequest = new SignupRequest();
        
        // Assert
        assertNull(signupRequest.getEmail());
        assertNull(signupRequest.getFirstName());
        assertNull(signupRequest.getLastName());
        assertNull(signupRequest.getPassword());
    }
    
    @Test
    public void testEquals() {
        // Arrange
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@test.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");
        
        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@test.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");
        
        // Act & Assert
        assertEquals(signupRequest1, signupRequest2);
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
    }
    
    @Test
    public void testNotEquals() {
        // Arrange
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test1@test.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");
        
        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test2@test.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");
        
        // Act & Assert
        assertNotEquals(signupRequest1, signupRequest2);
    }
    
    @Test
    public void testToString() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
        
        // Act
        String toString = signupRequest.toString();
        
        // Assert
        assertTrue(toString.contains("email=test@test.com"));
        assertTrue(toString.contains("firstName=John"));
        assertTrue(toString.contains("lastName=Doe"));
        assertTrue(toString.contains("password=password123"));
    }
}