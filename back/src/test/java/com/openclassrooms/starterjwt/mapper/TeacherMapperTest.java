package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testToDto() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(teacherDto);
        assertEquals(1L, teacherDto.getId());
        assertEquals("John", teacherDto.getFirstName());
        assertEquals("Doe", teacherDto.getLastName());
    }

    @Test
    public void testToEntity() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");

        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertNotNull(teacher);
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    public void testToDtoList() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // Act
        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        // Assert
        assertNotNull(teacherDtos);
        assertEquals(2, teacherDtos.size());
        
        assertEquals(1L, teacherDtos.get(0).getId());
        assertEquals("John", teacherDtos.get(0).getFirstName());
        assertEquals("Doe", teacherDtos.get(0).getLastName());
        
        assertEquals(2L, teacherDtos.get(1).getId());
        assertEquals("Jane", teacherDtos.get(1).getFirstName());
        assertEquals("Smith", teacherDtos.get(1).getLastName());
    }

    @Test
    public void testToEntityList() {
        // Arrange
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        // Act
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtos);

        // Assert
        assertNotNull(teachers);
        assertEquals(2, teachers.size());
        
        assertEquals(1L, teachers.get(0).getId());
        assertEquals("John", teachers.get(0).getFirstName());
        assertEquals("Doe", teachers.get(0).getLastName());
        
        assertEquals(2L, teachers.get(1).getId());
        assertEquals("Jane", teachers.get(1).getFirstName());
        assertEquals("Smith", teachers.get(1).getLastName());
    }
}
