package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoginRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testGetterAndSetter() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String email = "test@example.com";
        String password = "password123";

        // Act
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Assert
        assertEquals(email, loginRequest.getEmail(), "Email should be set and retrieved correctly");
        assertEquals(password, loginRequest.getPassword(), "Password should be set and retrieved correctly");
    }

    @Test
    public void testValidationWithValidData() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Act & Assert
        assertEquals(0, validator.validate(loginRequest).size(), "No validation errors should be present for valid data");
    }

    @Test
    public void testValidationWithBlankEmail() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");  // Blank email
        loginRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(loginRequest).size() > 0, "Validation should fail for blank email");
    }

    @Test
    public void testValidationWithBlankPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("");  // Blank password

        // Act & Assert
        assertTrue(validator.validate(loginRequest).size() > 0, "Validation should fail for blank password");
    }

    @Test
    public void testValidationWithNullFields() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        // Both fields null - default initialization

        // Act & Assert
        assertTrue(validator.validate(loginRequest).size() > 0, "Validation should fail for null fields");
    }
}
