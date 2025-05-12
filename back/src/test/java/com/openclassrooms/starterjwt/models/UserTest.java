package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class UserTest {

    @Test
    public void testUserConstructor() {
        // Test du constructeur par défaut
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testRequiredArgsConstructor() {
        // Test du constructeur avec arguments requis
        String email = "test@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password123";
        boolean admin = false;
        
        User user = new User(email, lastName, firstName, password, admin);
        
        assertEquals(email, user.getEmail());
        assertEquals(lastName, user.getLastName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
        assertEquals(admin, user.isAdmin());
    }

    @Test
    public void testBuilder() {
        // Test du Builder
        String email = "test@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password123";
        boolean admin = false;
        Long id = 1L;
        LocalDateTime now = LocalDateTime.now();
        
        User user = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(admin)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(lastName, user.getLastName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
        assertEquals(admin, user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }
    
    @Test
    public void testSettersAndGetters() {
        User user = new User();
        
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
        
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
        
        String lastName = "Doe";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
        
        String firstName = "John";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
        
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
        
        boolean admin = true;
        user.setAdmin(admin);
        assertEquals(admin, user.isAdmin());
        
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
        
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdatedAt());
    }
    
    @Test
    public void testEquals() {
        User user1 = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();
        
        User user2 = User.builder()
                .id(1L)
                .email("different@example.com") // différent email, mais même ID
                .lastName("Smith")
                .firstName("Jane")
                .password("password456")
                .admin(true)
                .build();
                
        User user3 = User.builder()
                .id(2L)
                .email("test@example.com") // même email, mais ID différent
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();
        
        // Les utilisateurs avec le même ID devraient être égaux
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        
        // Les utilisateurs avec des ID différents ne devraient pas être égaux
        assertNotEquals(user1, user3);
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
    
    @Test
    public void testToString() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();
        
        String userToString = user.toString();
        
        // Vérifier que la méthode toString contient les informations importantes
        assertTrue(userToString.contains("id=1"));
        assertTrue(userToString.contains("email=test@example.com"));
        assertTrue(userToString.contains("lastName=Doe"));
        assertTrue(userToString.contains("firstName=John"));
        assertTrue(userToString.contains("password=password123"));
        assertTrue(userToString.contains("admin=false"));
    }
    
    @Test
    public void testChaining() {
        User user = new User()
                .setEmail("test@example.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("password123")
                .setAdmin(true);
        
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
    }
}
