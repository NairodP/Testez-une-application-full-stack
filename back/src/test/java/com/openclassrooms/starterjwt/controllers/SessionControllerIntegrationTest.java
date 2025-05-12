package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@Transactional
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private Session testSession;
    private User testUser;
    private Teacher testTeacher;
    private String authToken;

    @BeforeEach
    public void setup() {
        // Nettoyer le contexte de sécurité
        SecurityContextHolder.clearContext();

        // Créer un utilisateur de test
        testUser = new User();
        testUser.setEmail("test-integration-controller@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("Controller");
        testUser.setPassword("password");
        testUser.setAdmin(true);
        testUser = userRepository.save(testUser);
        
        // Créer un enseignant de test
        testTeacher = new Teacher();
        testTeacher.setFirstName("Teacher");
        testTeacher.setLastName("Test");
        testTeacher = teacherRepository.save(testTeacher);

        // Créer une session de test
        testSession = new Session();
        testSession.setName("Session pour test d'intégration");
        testSession.setDescription("Description de la session de test d'intégration");
        testSession.setDate(new java.util.Date());
        testSession.setTeacher(testTeacher);
        testSession.setUsers(new ArrayList<>());
        testSession = sessionRepository.save(testSession);

        // Configurer l'authentification
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(testUser.getId())
                .username(testUser.getEmail())
                .firstName(testUser.getFirstName())
                .lastName(testUser.getLastName())
                .password(testUser.getPassword())
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testGetAllSessions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").exists());
    }

    @Test
    public void testGetSessionById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + testSession.getId())
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testSession.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testSession.getName())))
                .andExpect(jsonPath("$.description", is(testSession.getDescription())));
    }

    @Test
    public void testCreateSession() throws Exception {
        SessionDto newSessionDto = new SessionDto();
        newSessionDto.setName("Nouvelle session d'intégration");
        newSessionDto.setDescription("Description de la nouvelle session d'intégration");
        newSessionDto.setDate(new java.util.Date());
        newSessionDto.setTeacher_id(testTeacher.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nouvelle session d'intégration")))
                .andExpect(jsonPath("$.description", is("Description de la nouvelle session d'intégration")));
    }

    @Test
    public void testUpdateSession() throws Exception {
        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setId(testSession.getId());
        updatedSessionDto.setName("Session mise à jour");
        updatedSessionDto.setDescription("Description mise à jour");
        updatedSessionDto.setDate(new java.util.Date());
        updatedSessionDto.setTeacher_id(testTeacher.getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/" + testSession.getId())
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testSession.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Session mise à jour")))
                .andExpect(jsonPath("$.description", is("Description mise à jour")));
    }

    @Test
    public void testDeleteSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + testSession.getId())
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testParticipateInSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/" + testSession.getId() + "/participate/" + testUser.getId())
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testNoLongerParticipateInSession() throws Exception {
        // D'abord participer
        testSession.getUsers().add(testUser);
        sessionRepository.save(testSession);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + testSession.getId() + "/participate/" + testUser.getId())
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());
    }
}
