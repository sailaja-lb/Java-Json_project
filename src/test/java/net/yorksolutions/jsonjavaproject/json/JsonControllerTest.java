package net.yorksolutions.jsonjavaproject.json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JsonControllerTest {
    @Mock
    HttpServletRequest request;

    @InjectMocks
    JsonController controller;
    @Spy
    JsonController spyController;

    @Mock
    RestTemplate rest;

    @BeforeEach
    public void initialSpy() {
        spyController = Mockito.spy(new JsonController());
    }


    @Test
    void itShouldCallIpAddressAndReturnAnIpAddress() {
        final var token = UUID.randomUUID();
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        Mockito.doNothing().when(spyController).checkAuthorized(captor.capture());
        Assertions.assertEquals(IP.class, spyController.ip(token, request).getClass());
        assertEquals(token, captor.getValue());
    }

    @Test
    void itShouldCallDateTimeAndReturnDateTime() {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        final var token = UUID.randomUUID();
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        Mockito.doNothing().when(spyController).checkAuthorized(captor.capture());
        Assertions.assertEquals(todayDate, spyController.dateTime(token, request).date);
        Assertions.assertEquals(currentTime, spyController.dateTime(token, request).time);
    }


    @Test
    void ItShouldReturnHeadersWhenCalled() {
        final var token = UUID.randomUUID();
        Map<String, String> expected = new HashMap<>();
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        Mockito.doNothing().when(spyController).checkAuthorized(captor.capture());
        Assertions.assertEquals(expected, spyController.headers(token, expected));

    }


    @Test
    void itShouldReturnMd5Result() throws NoSuchAlgorithmException {
        final var token = UUID.randomUUID();
        String original = "abc";
        String md5 = "900150983cd24fb0d6963f7d28e17f72".toLowerCase();
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        Mockito.doNothing().when(spyController).checkAuthorized(captor.capture());
        Md5 md5Func = spyController.md5(token, original);
        Assertions.assertEquals(Md5.class, md5Func.getClass());
        Assertions.assertEquals(md5, md5Func.md5);
        Assertions.assertEquals(original, md5Func.original);
    }

    @Test
    void itShouldReturnArrayWhenGivenArray() {
        final var token = UUID.randomUUID();
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        Mockito.doNothing().when(spyController).checkAuthorized(captor.capture());
        String testArray = "[1,2,3]";
        JSONValidate test = spyController.validate(token, testArray);
        Assertions.assertEquals("array", test.object_or_array);
    }
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


//    @Test
//    void itShouldDelTokenWhenLogout() {
//        when(repository.findByUsernameAndPassword("user1", "pass1")).thenReturn(Optional.of(
//                new UserAccount()));
//        UUID login1 = controller.login("user1", "pass1");
//        tokenMap.put(login1, 1L);
//        when(repository.findByUsernameAndPassword("user2", "pass2")).thenReturn(Optional.of(
//                new UserAccount()));
//        UUID login2 = controller.login("user2", "pass2");
//        tokenMap.put(login2, 2L);
//        System.out.println("tokenMap");
//        controller.logout(login1);
//        when(tokenMap.containsKey(login2)).thenReturn(true);
//        assertTrue(tokenMap.containsKey(login2));
//    }
//    @Test
//    void itShouldDeleteTokenWhenLogout() {
//        final Long id = (long) (Math.random() * 9999999);
//        HashMap<UUID, Long> expected = new HashMap<>();
//        UUID token = UUID.randomUUID();
////        tokenMap.put(token, id);
//        assertEquals(expected, controller.logout(token));
//    }
//    @Test
//    void itShouldLogoutWithGivenToken() {
//        final var token = UUID.randomUUID();
//        tokenMap.put(token, 1L);
//        controller.logout(token);
//        Assertions.assertFalse(tokenMap.containsKey(token));
//    }
//    @Test
//    void itShouldThrowUnauthWhenOtherStatusIsUnauth() {
//        final UUID token = UUID.randomUUID();
//        String url = "http://localhost:8081/isAuthorized?token=" + token;
//
////        {
////            RestTemplate rest;
////            rest = new RestTemplate();
////            ResponseEntity<Void> response = rest.getForEntity(url, Void.class, (Object) null);
////            response.getStatusCode(); // do something with that status code
////        }
//
//    when(rest.getForEntity(url, Void.class))
//            .thenReturn(new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED));
//    final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
//            () -> controller.checkAuthorized(token));
//    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
//}

//    @Test
//    void itShouldThrowIntErrWhenOtherStatusIsOther() {
//        final UUID token = UUID.randomUUID();
//        String url = "http://localhost:8081/isAuthorized?token=" + token;
//        when(rest.getForEntity(url, Void.class))
//                .thenReturn(new ResponseEntity<Void>(HttpStatus.CONFLICT));
//        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
//                () -> controller.checkAuthorized(token));
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
//    }

//    @Test
//    void itShouldNotThrowWhenOtherStatusIsOK() {
//        final UUID token = UUID.randomUUID();
//        String url = "http://localhost:8081/isAuthorized?token=" + token;
//        when(rest.getForEntity(url, Void.class))
//                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
//        assertDoesNotThrow(() -> controller.checkAuthorized(token));
//    }


//    @Test
//    void itShouldReturnClientsIp() {
//        String ip = "127.0.01";
//        IP expectedIp = new IP(ip);
//        var token = UUID.randomUUID();
//        when(request.getRemoteAddr()).thenReturn(ip);
//        when(tokenMap.containsKey(token)).thenReturn(true);
//        Assertions.assertEquals(expectedIp, controller.ip(token, request));
//    }

//    @Test
//    public void datetimeTest(){
//        //DateTime dateTime = controller.dateTime(request);
//        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
//        var token = UUID.randomUUID();
//        when(tokenMap.containsKey(token)).thenReturn(true);
//        Assertions.assertEquals(todayDate, controller.dateTime(token, request).date);
//        Assertions.assertEquals(currentTime, controller.dateTime(token, request).time);
//    }
//    @Test
//    void itShouldThrowUnauthorizedWhenDateTimeIsCalledWIthBadToken() {
//        final var token = UUID.randomUUID();
//        when(tokenMap.containsKey(token)).thenReturn(false);
//        assertThrows(ResponseStatusException.class, () -> controller.dateTime(token, request));
//    }
//headers test
//    @Test
//    void ItShouldReturnHeadersWhenCalled() {
//        Map<String, String> expected = new HashMap<>();
//        expected.put("header", "header1");
//        final var token = UUID.randomUUID();
//        when(tokenMap.containsKey(token)).thenReturn(true);
//        Assertions.assertEquals(expected, controller.headers(token, expected));
//
//    }
//    @Test
//    void itShouldThrowUnauthorizedWhenHeadersIsCalledWIthBadToken() {
//        Map<String, String> expected = new HashMap<>();
//        expected.put("header", "header1");
//        final var token = UUID.randomUUID();
//        when(tokenMap.containsKey(token)).thenReturn(false);
//        assertThrows(ResponseStatusException.class, () -> controller.headers(token, expected));
//    }
//Md5 test
//    @Test
//    void itShouldHAshAbcCorrect() throws NoSuchAlgorithmException {
//        //String expected = "900150983cd24fb0d6963f7d28e17f72";
//        Map<String, String> textMd5 = new HashMap<>();
//        textMd5.put("md5", "900150983cd24fb0d6963f7d28e17f72");
//        final var token = UUID.randomUUID();
//        //when(text).thenReturn(ip);
//        when(tokenMap.containsKey(token)).thenReturn(true);
//        Map<String, String> result = (Map<String, String>) controller.md5(token,"abc");
//        Assertions.assertEquals("md5",result);
//    }
//@Test
//void itShouldReturnUnauthorizedWhenUserIsWrong() {
//    final var username = UUID.randomUUID().toString();
//    final var password = UUID.randomUUID().toString();
//    // ArgumentMatcher - it is a test that can be run on an argument
//    // any() - always return true
//    // eq(<expected>) - the passed argument must match <expected>
//    lenient().when(repository.findByUsernameAndPassword(username, password))
//            .thenReturn(Optional.empty());
//    // lenient() - I know that a stubbing will not be called in the passing case,
//    //      but I want to test for it anyway
//    lenient().when(repository.findByUsernameAndPassword(not(eq(username)), eq(password)))
//            .thenReturn(Optional.of(new UserAccount()));
//    assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
//}
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
//
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
//    //register
//    @Test
//    void itShouldReturnInvalidIfUsernameExists() {
//        final String username = "some username";
//        when(repository.findByUsername(username)).thenReturn(Optional.of(
//                new UserAccount()));
//        assertThrows(ResponseStatusException.class, () -> {
//            controller.register(username, "");
//        });
//    }
//
//    @Test
//    void itShouldSaveANewUserAccountWhenUserIsUnique() {
//        final String username = "some username";
//        final String password = "some password";
//        when(repository.findByUsername(username)).thenReturn(Optional.empty());
//        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
//        when(repository.save(captor.capture())).thenReturn(new UserAccount());
//        Assertions.assertDoesNotThrow(() -> {
//            controller.register(username, password);
//        });
//        assertEquals(new UserAccount(), captor.getValue());
//    }
//
//    @Test
//    void itShouldThrowUnauthorizedWhenIPIsCalledWithBadToken() {
//        final var token = UUID.randomUUID();
//        when(tokenMap.containsKey(token)).thenReturn(false);
//        assertThrows(ResponseStatusException.class, () -> controller.ip(token, request));
//    }
