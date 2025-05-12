package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtResponseTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String token = "jwt-token-123";
        Long id = 1L;
        String username = "john.doe@example.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;

        // Act
        JwtResponse jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        // Assert
        assertEquals(token, jwtResponse.getToken(), "Token should be set correctly");
        assertEquals("Bearer", jwtResponse.getType(), "Type should be 'Bearer' by default");
        assertEquals(id, jwtResponse.getId(), "Id should be set correctly");
        assertEquals(username, jwtResponse.getUsername(), "Username should be set correctly");
        assertEquals(firstName, jwtResponse.getFirstName(), "FirstName should be set correctly");
        assertEquals(lastName, jwtResponse.getLastName(), "LastName should be set correctly");
        assertEquals(admin, jwtResponse.getAdmin(), "Admin flag should be set correctly");
    }

    @Test
    public void testSetters() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("original-token", 1L, "original@example.com", "Original", "User", false);
        
        String newToken = "new-jwt-token";
        String newType = "NewBearer";
        Long newId = 2L;
        String newUsername = "new@example.com";
        String newFirstName = "New";
        String newLastName = "User";
        Boolean newAdmin = true;

        // Act
        jwtResponse.setToken(newToken);
        jwtResponse.setType(newType);
        jwtResponse.setId(newId);
        jwtResponse.setUsername(newUsername);
        jwtResponse.setFirstName(newFirstName);
        jwtResponse.setLastName(newLastName);
        jwtResponse.setAdmin(newAdmin);

        // Assert
        assertEquals(newToken, jwtResponse.getToken(), "Token should be updated correctly");
        assertEquals(newType, jwtResponse.getType(), "Type should be updated correctly");
        assertEquals(newId, jwtResponse.getId(), "Id should be updated correctly");
        assertEquals(newUsername, jwtResponse.getUsername(), "Username should be updated correctly");
        assertEquals(newFirstName, jwtResponse.getFirstName(), "FirstName should be updated correctly");
        assertEquals(newLastName, jwtResponse.getLastName(), "LastName should be updated correctly");
        assertEquals(newAdmin, jwtResponse.getAdmin(), "Admin flag should be updated correctly");
    }
}
