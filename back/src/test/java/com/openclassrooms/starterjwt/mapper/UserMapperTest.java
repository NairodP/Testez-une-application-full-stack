package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testToDto() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("encodedPassword");
        user.setAdmin(false);

        // Act
        UserDto userDto = userMapper.toDto(user);

        // Assert
        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("test@test.com", userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertFalse(userDto.isAdmin());
        // Le mot de passe est transféré au DTO dans l'implémentation actuelle
        assertEquals("encodedPassword", userDto.getPassword());
    }

    @Test
    public void testToEntity() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(false);
        userDto.setPassword("testPassword");

        // Act
        User user = userMapper.toEntity(userDto);

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(false, user.isAdmin());
        // Le mot de passe est conservé lors de la conversion de DTO à entité
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testToDtoList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@test.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@test.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        List<User> users = Arrays.asList(user1, user2);

        // Act
        List<UserDto> userDtos = userMapper.toDto(users);

        // Assert
        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());
        
        assertEquals(1L, userDtos.get(0).getId());
        assertEquals("test1@test.com", userDtos.get(0).getEmail());
        assertEquals("John", userDtos.get(0).getFirstName());
        assertEquals("Doe", userDtos.get(0).getLastName());
        
        assertEquals(2L, userDtos.get(1).getId());
        assertEquals("test2@test.com", userDtos.get(1).getEmail());
        assertEquals("Jane", userDtos.get(1).getFirstName());
        assertEquals("Smith", userDtos.get(1).getLastName());
    }

    @Test
    public void testToEntityList() {
        // Arrange
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("test1@test.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setAdmin(false);
        userDto1.setPassword("password1");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("test2@test.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setAdmin(false);
        userDto2.setPassword("password2");

        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        // Act
        List<User> users = userMapper.toEntity(userDtos);

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        
        assertEquals(1L, users.get(0).getId());
        assertEquals("test1@test.com", users.get(0).getEmail());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Doe", users.get(0).getLastName());
        
        assertEquals(2L, users.get(1).getId());
        assertEquals("test2@test.com", users.get(1).getEmail());
        assertEquals("Jane", users.get(1).getFirstName());
        assertEquals("Smith", users.get(1).getLastName());
    }
}
