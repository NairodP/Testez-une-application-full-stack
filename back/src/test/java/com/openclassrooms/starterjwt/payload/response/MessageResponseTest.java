package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageResponseTest {

    @Test
    public void testConstructorAndGetter() {
        // Arrange
        String message = "Operation successful";

        // Act
        MessageResponse messageResponse = new MessageResponse(message);

        // Assert
        assertEquals(message, messageResponse.getMessage(), "Message should be set correctly via constructor");
    }

    @Test
    public void testSetter() {
        // Arrange
        MessageResponse messageResponse = new MessageResponse("Initial message");
        String newMessage = "Updated message";

        // Act
        messageResponse.setMessage(newMessage);

        // Assert
        assertEquals(newMessage, messageResponse.getMessage(), "Message should be updated correctly via setter");
    }
}
