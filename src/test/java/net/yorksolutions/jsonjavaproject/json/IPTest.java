package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IPTest {
    @Test
    void itShouldReturnJsonifyIpAddress() throws JsonProcessingException {
        IP ip = new IP("127.0.0.1");
        String result = new ObjectMapper().writeValueAsString(ip);
        assertEquals("{\"ip\":\"127.0.0.1\"}", result);
    }

}
