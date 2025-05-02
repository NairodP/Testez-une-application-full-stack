package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    private Session testSession;
    private SessionDto testSessionDto;
    private List<Session> testSessions;
    private List<SessionDto> testSessionDtos;

    @BeforeEach
    public void setup() {
        // Création d'un professeur de test
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Création d'un utilisateur de test
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setFirstName("User");
        user.setLastName("Test");
        
        List<User> users = new ArrayList<>();
        users.add(user);

        // Configuration de la session de test
        testSession = new Session();
        testSession.setId(1L);
        testSession.setName("Yoga Session");
        testSession.setDate(new Date());
        testSession.setDescription("A yoga session for beginners");
        testSession.setTeacher(teacher);
        testSession.setUsers(users);

        // Configuration du DTO de session
        testSessionDto = new SessionDto();
        testSessionDto.setId(1L);
        testSessionDto.setName("Yoga Session");
        testSessionDto.setDate(new Date());
        testSessionDto.setDescription("A yoga session for beginners");
        testSessionDto.setTeacher_id(1L);
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        testSessionDto.setUsers(userIds);

        // Création d'une autre session pour la liste
        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Pilates Session");
        session2.setDate(new Date());
        session2.setDescription("A pilates session for beginners");
        session2.setTeacher(teacher);
        session2.setUsers(new ArrayList<>());

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Pilates Session");
        sessionDto2.setDate(new Date());
        sessionDto2.setDescription("A pilates session for beginners");
        sessionDto2.setTeacher_id(1L);
        sessionDto2.setUsers(new ArrayList<>());

        // Création des listes pour findAll
        testSessions = Arrays.asList(testSession, session2);
        testSessionDtos = Arrays.asList(testSessionDto, sessionDto2);
    }

    @Test
    public void testFindByIdSuccess() throws Exception {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        // Act & Assert
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Yoga Session")))
                .andExpect(jsonPath("$.description", is("A yoga session for beginners")))
                .andExpect(jsonPath("$.teacher_id", is(1)));

        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(testSession);
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        // Arrange
        when(sessionService.getById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/session/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(999L);
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testFindByIdBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).getById(anyLong());
    }

    @Test
    public void testFindAll() throws Exception {
        // Arrange
        when(sessionService.findAll()).thenReturn(testSessions);
        when(sessionMapper.toDto(testSessions)).thenReturn(testSessionDtos);

        // Act & Assert
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Yoga Session")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Pilates Session")));

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(testSessions);
    }

    @Test
    public void testCreateSession() throws Exception {
        // Arrange
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(testSession);
        when(sessionService.create(any(Session.class))).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        // Act & Assert
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Yoga Session")));

        verify(sessionMapper).toEntity(any(SessionDto.class));
        verify(sessionService).create(any(Session.class));
        verify(sessionMapper).toDto(testSession);
    }

    @Test
    public void testUpdateSession() throws Exception {
        // Arrange
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(testSession);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        // Act & Assert
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Yoga Session")));

        verify(sessionMapper).toEntity(any(SessionDto.class));
        verify(sessionService).update(eq(1L), any(Session.class));
        verify(sessionMapper).toDto(testSession);
    }

    @Test
    public void testUpdateSessionBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/session/invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSessionDto)))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).update(anyLong(), any(Session.class));
    }

    @Test
    public void testDeleteSession() throws Exception {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(testSession);

        // Act & Assert
        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
    }

    @Test
    public void testDeleteSessionNotFound() throws Exception {
        // Arrange
        when(sessionService.getById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/session/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(999L);
        verify(sessionService, never()).delete(999L);
    }

    @Test
    public void testDeleteSessionBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).getById(anyLong());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void testParticipate() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/session/1/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).participate(1L, 1L);
    }

    @Test
    public void testParticipateBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/session/invalid/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    public void testNoLongerParticipate() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/1/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(1L, 1L);
    }

    @Test
    public void testNoLongerParticipateBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/invalid/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }
}