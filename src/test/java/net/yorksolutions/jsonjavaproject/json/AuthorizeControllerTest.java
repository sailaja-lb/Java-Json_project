package net.yorksolutions.jsonjavaproject.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizeControllerTest {

    @InjectMocks
    AuthorizeController controller;
    @Mock
    UserAccountRepository repository;
    @Mock
    HashMap<UUID, Long> tokenMap;

    @Mock
    RestTemplate rest;

    // The fizz controller will ask another controller if this user is authorized
    // URL of the other controller
    // Pass the token that the user is providing
    // Possible reponses:
    // 1. OK - the user is authorized, we can proceed
    // 2. UNAUTHORIZED - we can throw UNAUTH
    // 3. Some other status code - throw INTERNAL_SERVER_ERROR


//    @Test
//    void itShouldSaveANewUserAccountWhenUserIsUnique() {
//        final String username = "some username";
//        final String password = "some password";
//        when(repository.findByUsername(username)).thenReturn(Optional.empty());
//        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
//        when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password));
//        Assertions.assertDoesNotThrow(() -> {
//            controller.register(username, password);
//        });
//        assertEquals(new UserAccount(username, password), captor.getValue());
//    }
//
//    //register
//    @Test
//    void itShouldReturnInvalidIfUsernameExists() {
//        final String username = "some username";
//        when(repository.findByUsername(username)).thenReturn(Optional.of(
//                new UserAccount(username, "")));
//        assertThrows(ResponseStatusException.class, () -> {
//            controller.register(username, "");
//        });
//    }
//    @Test
//    void itShouldReturnUnauthorizedWhenUserIsWrong() {
//        final var username = UUID.randomUUID().toString();
//        final var password = UUID.randomUUID().toString();
//        // ArgumentMatcher - it is a test that can be run on an argument
//        // any() - always return true
//        // eq(<expected>) - the passed argument must match <expected>
//        lenient().when(repository.findByUsernameAndPassword(username, password))
//                .thenReturn(Optional.empty());
//        // lenient() - I know that a stubbing will not be called in the passing case,
//        //      but I want to test for it anyway
//        lenient().when(repository.findByUsernameAndPassword(not(eq(username)), eq(password)))
//                .thenReturn(Optional.of(new UserAccount(username, password)));
//        assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
//    }
//
//    @Test
//    void itShouldReturnUnauthorizedWhenPassIsWrong() {
//        final var username = UUID.randomUUID().toString();
//        final var password = UUID.randomUUID().toString();
//        lenient().when(repository.findByUsernameAndPassword(username, password))
//                .thenReturn(Optional.empty());
//        lenient().when(repository.findByUsernameAndPassword(eq(username), not(eq(password))))
//                .thenReturn(Optional.of(new UserAccount()));
//        assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
//    }
//    @Test
//    void itShouldMapTheUUIDToTheIdWhenLoginSuccess() {
//        final var username = UUID.randomUUID().toString();
//        final var password = UUID.randomUUID().toString();
//        final Long id = (long) (Math.random() * 9999999); // the id of the user account associated with username, password
//        final UserAccount expected = new UserAccount();
//        expected.id = id;
//        expected.username = username;
//        expected.password = password;
//        when(repository.findByUsernameAndPassword(username, password))
//                .thenReturn(Optional.of(expected));
//        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
//        when(tokenMap.put(captor.capture(), eq(id))).thenReturn(0L);
//        final var token = controller.login(username, password);
//        assertEquals(token, captor.getValue());
//    }
//
//    @Test
//    void itShouldDeleteTokenWhenLogout() {
//        final Long id = (long) (Math.random() * 9999999);
//        final UUID token = UUID.randomUUID();
//        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
//        when(tokenMap.remove(captor.capture())).thenReturn(id);
//        controller.logout(token);
//        assertEquals(token, captor.getValue());
//    }

}
