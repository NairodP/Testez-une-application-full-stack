package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setup() {
        // Configuration de l'utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("encodedPassword");
        testUser.setAdmin(false);
    }

    @Test
    public void testLoadUserByUsername() {
        // Arrange
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@test.com");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof UserDetailsImpl);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        
        assertEquals(1L, userDetailsImpl.getId());
        assertEquals("test@test.com", userDetailsImpl.getUsername());
        assertEquals("John", userDetailsImpl.getFirstName());
        assertEquals("Doe", userDetailsImpl.getLastName());
        assertEquals("encodedPassword", userDetailsImpl.getPassword());
        
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    public void testLoadUserByUsernameThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("nonexistent@test.com")
        );
        
        assertTrue(exception.getMessage().contains("User Not Found with email: nonexistent@test.com"));
        verify(userRepository).findByEmail("nonexistent@test.com");
    }
}
