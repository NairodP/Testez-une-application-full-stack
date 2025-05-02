package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String email = "test@test.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setAdmin(admin);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(lastName, user.getLastName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    public void testBuilderPattern() {
        // Arrange & Act
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertTrue(user.isAdmin());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        User user1 = User.builder().id(1L).email("test@test.com").lastName("Doe").firstName("John").password("password").admin(false).build();
        User user2 = User.builder().id(1L).email("different@test.com").lastName("Different").firstName("Name").password("different").admin(true).build();
        User user3 = User.builder().id(2L).email("test@test.com").lastName("Doe").firstName("John").password("password").admin(false).build();

        // Act & Assert
        assertEquals(user1, user2, "Users with the same id should be equal");
        assertNotEquals(user1, user3, "Users with different ids should not be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Users with the same id should have the same hashCode");
        assertNotEquals(user1.hashCode(), user3.hashCode(), "Users with different ids should have different hashCodes");
    }

    @Test
    public void testToString() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(true)
                .build();

        // Act
        String toString = user.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("email=test@test.com"));
        assertTrue(toString.contains("lastName=Doe"));
        assertTrue(toString.contains("firstName=John"));
        assertTrue(toString.contains("admin=true"));
    }

    @Test
    public void testRequiredArgsConstructor() {
        // Arrange & Act
        User user = new User(
                "test@test.com",
                "Doe",
                "John",
                "password",
                true
        );

        // Assert
        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
        assertNull(user.getId());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String email = "test@test.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        User user = new User(id, email, lastName, firstName, password, admin, createdAt, updatedAt);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(lastName, user.getLastName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }
}