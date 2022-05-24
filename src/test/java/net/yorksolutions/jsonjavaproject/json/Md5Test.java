package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Md5Test {
    @Test
    void itShouldConvertStringToMd5() throws NoSuchAlgorithmException, JsonProcessingException {
        Md5 md = new Md5("abc");
        String result = new ObjectMapper().writeValueAsString(md);
        assertEquals("{\"original\":\"abc\",\"md5\":\"900150983cd24fb0d6963f7d28e17f72\"}", result);
    }
}
