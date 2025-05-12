package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User testUser;
    private UserDto testUserDto;
    private UserDetailsImpl userDetails;

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

        // Configuration du DTO d'utilisateur
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@test.com");
        testUserDto.setFirstName("John");
        testUserDto.setLastName("Doe");
        testUserDto.setAdmin(false);

        // Configuration des détails d'utilisateur pour l'authentification
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword")
                .build();
    }

    @Test
    public void testFindByIdReturnsUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserDto, response.getBody());
        verify(userService).findById(1L);
        verify(userMapper).toDto(testUser);
    }

    @Test
    public void testFindByIdReturnsNotFoundWhenUserNotFound() {
        // Arrange
        when(userService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById("999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(999L);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testFindByIdReturnsBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(anyLong());
    }

    @Test
    public void testDeleteUserSuccess() {
        // Arrange
        when(userService.findById(1L)).thenReturn(testUser);
        doNothing().when(userService).delete(1L);

        // Configuration du contexte d'authentification
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    public void testDeleteUserReturnsNotFoundWhenUserNotFound() {
        // Arrange
        when(userService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.save("999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(999L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteUserReturnsBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = userController.save("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(anyLong());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteUserReturnsUnauthorizedWhenDifferentUser() {
        // Arrange
        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setEmail("different@test.com");

        when(userService.findById(2L)).thenReturn(differentUser);

        // Configuration du contexte d'authentification
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails); // Connecté en tant que test@test.com

        // Act
        ResponseEntity<?> response = userController.save("2"); // Essayer de supprimer different@test.com

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).findById(2L);
        verify(userService, never()).delete(anyLong());
    }
}
