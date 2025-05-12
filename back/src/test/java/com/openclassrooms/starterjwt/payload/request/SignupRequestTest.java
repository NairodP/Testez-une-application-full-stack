package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SignupRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testGetterAndSetter() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password123";

        // Act
        signupRequest.setEmail(email);
        signupRequest.setFirstName(firstName);
        signupRequest.setLastName(lastName);
        signupRequest.setPassword(password);

        // Assert
        assertEquals(email, signupRequest.getEmail(), "Email should be set and retrieved correctly");
        assertEquals(firstName, signupRequest.getFirstName(), "FirstName should be set and retrieved correctly");
        assertEquals(lastName, signupRequest.getLastName(), "LastName should be set and retrieved correctly");
        assertEquals(password, signupRequest.getPassword(), "Password should be set and retrieved correctly");
    }

    @Test
    public void testValidationWithValidData() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act & Assert
        assertEquals(0, validator.validate(signupRequest).size(), "No validation errors should be present for valid data");
    }

    @Test
    public void testValidationWithInvalidEmail() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");  // Not a valid email format
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for invalid email format");
    }

    @Test
    public void testValidationWithBlankEmail() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("");  // Blank email
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for blank email");
    }

    @Test
    public void testValidationWithShortFirstName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Jo");  // Too short (min 3)
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for first name that is too short");
    }

    @Test
    public void testValidationWithLongFirstName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("J".repeat(21));  // Too long (max 20)
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for first name that is too long");
    }

    @Test
    public void testValidationWithShortLastName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Do");  // Too short (min 3)
        signupRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for last name that is too short");
    }

    @Test
    public void testValidationWithLongLastName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("D".repeat(21));  // Too long (max 20)
        signupRequest.setPassword("password123");

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for last name that is too long");
    }

    @Test
    public void testValidationWithShortPassword() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("12345");  // Too short (min 6)

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for password that is too short");
    }

    @Test
    public void testValidationWithLongPassword() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("1".repeat(41));  // Too long (max 40)

        // Act & Assert
        assertTrue(validator.validate(signupRequest).size() > 0, "Validation should fail for password that is too long");
    }

    @Test
    public void testToString() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act & Assert
        String toString = signupRequest.toString();
        assertTrue(toString.contains("test@example.com"), "toString should contain the email");
        assertTrue(toString.contains("John"), "toString should contain the first name");
        assertTrue(toString.contains("Doe"), "toString should contain the last name");
        assertTrue(toString.contains("password"), "toString should contain the password field name");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        SignupRequest differentSignupRequest = new SignupRequest();
        differentSignupRequest.setEmail("different@example.com");
        differentSignupRequest.setFirstName("Jane");
        differentSignupRequest.setLastName("Smith");
        differentSignupRequest.setPassword("different123");

        // Act & Assert
        assertEquals(signupRequest1, signupRequest2, "Equal SignupRequest objects should be equal");
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Equal SignupRequest objects should have the same hashCode");
        assertNotEquals(signupRequest1, differentSignupRequest, "Different SignupRequest objects should not be equal");
    }
}
