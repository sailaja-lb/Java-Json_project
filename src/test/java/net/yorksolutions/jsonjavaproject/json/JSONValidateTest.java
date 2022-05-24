package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONValidateTest {
    @Test
    void itShouldValidateWhenArrayIsGiven() throws JsonProcessingException {
        String input = "[1,2,3]";
        JSONValidate validate = new JSONValidate(input);
        assertEquals(true, validate.validate);
        assertEquals("array", validate.object_or_array);
        assertEquals(3, validate.size);
        assertEquals(false, validate.empty);
        assertEquals(null, validate.error);
    }

    @Test
    void itShouldValidateWhenObjectIsGiven() throws JsonProcessingException {
        String input = "{\"key\":\"value\"}";
        JSONValidate validate = new JSONValidate(input);
        assertEquals(true, validate.validate);
        assertEquals("object", validate.object_or_array);
        assertEquals(1, validate.size);
        assertEquals(false, validate.empty);
        assertEquals(null, validate.error);
    }

    @Test
    void itShouldValidateWhenEmptyObjectIsGiven() throws JsonProcessingException {
        String input = "{}";
        JSONValidate validate = new JSONValidate(input);
        assertEquals(true, validate.validate);
        assertEquals("object", validate.object_or_array);
        assertEquals(0, validate.size);
        assertEquals(true, validate.empty);
        assertEquals(null, validate.error);
    }
}
