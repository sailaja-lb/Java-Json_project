package net.yorksolutions.jsonjavaproject.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizeControllerTest {

    @InjectMocks
    AuthorizeController controller;

    @Mock
    RestTemplate rest;

    // The fizz controller will ask another controller if this user is authorized
    // URL of the other controller
    // Pass the token that the user is providing
    // Possible reponses:
    // 1. OK - the user is authorized, we can proceed
    // 2. UNAUTHORIZED - we can throw UNAUTH
    // 3. Some other status code - throw INTERNAL_SERVER_ERROR

    @Test
    void itShouldThrowUnauthWhenOtherStatusIsUnauth() {
        final UUID token = UUID.randomUUID();
        String url = "http://localhost:8081/isAuthorized?token=" + token;

//        {
//            RestTemplate rest;
//            rest = new RestTemplate();
//            ResponseEntity<Void> response = rest.getForEntity(url, Void.class, (Object) null);
//            response.getStatusCode(); // do something with that status code
//        }

        when(rest.getForEntity(url, Void.class))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED));
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.checkAuthorized(token));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void itShouldThrowIntErrWhenOtherStatusIsOther() {
        final UUID token = UUID.randomUUID();
        String url = "http://localhost:8081/isAuthorized?token=" + token;
        when(rest.getForEntity(url, Void.class))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.CONFLICT));
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.checkAuthorized(token));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    void itShouldNotThrowWhenOtherStatusIsOK() {
        final UUID token = UUID.randomUUID();
        String url = "http://localhost:8081/isAuthorized?token=" + token;
        when(rest.getForEntity(url, Void.class))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        assertDoesNotThrow(() -> controller.checkAuthorized(token));
    }

}
