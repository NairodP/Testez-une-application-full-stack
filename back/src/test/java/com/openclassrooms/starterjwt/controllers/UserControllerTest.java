package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User testUser;
    private UserDto testUserDto;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setup() {
        // Configuration du user de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("encodedPassword");
        testUser.setAdmin(false);

        // Configuration du DTO
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@test.com");
        testUserDto.setFirstName("John");
        testUserDto.setLastName("Doe");
        testUserDto.setPassword("encodedPassword");
        testUserDto.setAdmin(false);

        // Configuration de l'authentification
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testFindByIdSuccess() throws Exception {
        // Arrange
        when(userService.findById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act & Assert
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findById(1L);
        verify(userMapper).toDto(testUser);
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        // Arrange
        when(userService.findById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/user/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).findById(999L);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testFindByIdBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/user/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyLong());
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        // Arrange
        when(userService.findById(1L)).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    public void testDeleteUnauthorized() throws Exception {
        // Arrange
        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setEmail("different@test.com");
        differentUser.setFirstName("Different");
        differentUser.setLastName("User");

        when(userService.findById(2L)).thenReturn(differentUser);

        // Act & Assert
        mockMvc.perform(delete("/api/user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userService).findById(2L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        // Arrange
        when(userService.findById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/user/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).findById(999L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/user/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyLong());
        verify(userService, never()).delete(anyLong());
    }
}