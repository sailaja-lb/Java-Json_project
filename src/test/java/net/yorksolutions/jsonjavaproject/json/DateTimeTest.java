package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//import static org.junit.jupiter.api.AssertTrue.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeTest {
    @Test
    void itShouldReturnDateAndTime() throws JsonProcessingException {
        DateTime dt = new DateTime("2022-05-20", "12-10-20");
        String result = new ObjectMapper().writeValueAsString(dt);
        assertEquals("{\"date\":\"2022-05-20\",\"time\":\"12-10-20\"}", result);
    }

//    @Test
//    void itShouldTestTheDateFormatter() {
//        DateTime dt = new DateTime();
//       Assertions.assertTrue(GenericValidator.isDate("2019-02-28", "yyyy-MM-dd", true));
//    }

}
