package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtResponseTest {

    @Test
    public void testConstructor() {
        // Arrange
        String token = "mockToken";
        Long id = 1L;
        String username = "test@test.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;

        // Act
        JwtResponse jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        // Assert
        assertEquals(token, jwtResponse.getToken());
        assertEquals(id, jwtResponse.getId());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(firstName, jwtResponse.getFirstName());
        assertEquals(lastName, jwtResponse.getLastName());
        assertEquals(admin, jwtResponse.getAdmin());
        assertEquals("Bearer", jwtResponse.getType());
    }

    @Test
    public void testSetAndGetToken() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("oldToken", 1L, "user", "John", "Doe", false);
        String newToken = "newToken";

        // Act
        jwtResponse.setToken(newToken);

        // Assert
        assertEquals(newToken, jwtResponse.getToken());
    }

    @Test
    public void testSetAndGetType() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("token", 1L, "user", "John", "Doe", false);
        String newType = "NewType";

        // Act
        jwtResponse.setType(newType);

        // Assert
        assertEquals(newType, jwtResponse.getType());
    }

    @Test
    public void testSetAndGetId() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("token", 1L, "user", "John", "Doe", false);
        Long newId = 2L;

        // Act
        jwtResponse.setId(newId);

        // Assert
        assertEquals(newId, jwtResponse.getId());
    }

    @Test
    public void testSetAndGetUsername() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("token", 1L, "oldUser", "John", "Doe", false);
        String newUsername = "newUser";

        // Act
        jwtResponse.setUsername(newUsername);

        // Assert
        assertEquals(newUsername, jwtResponse.getUsername());
    }

    @Test
    public void testSetAndGetFirstName() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("token", 1L, "user", "John", "Doe", false);
        String newFirstName = "Jane";

        // Act
        jwtResponse.setFirstName(newFirstName);

        // Assert
        assertEquals(newFirstName, jwtResponse.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("token", 1L, "user", "John", "Doe", false);
        String newLastName = "Smith";

        // Act
        jwtResponse.setLastName(newLastName);

        // Assert
        assertEquals(newLastName, jwtResponse.getLastName());
    }

    @Test
    public void testSetAndGetAdmin() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("token", 1L, "user", "John", "Doe", false);
        Boolean newAdmin = true;

        // Act
        jwtResponse.setAdmin(newAdmin);

        // Assert
        assertEquals(newAdmin, jwtResponse.getAdmin());
    }
}