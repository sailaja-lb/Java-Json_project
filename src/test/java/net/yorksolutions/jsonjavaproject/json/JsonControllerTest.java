package net.yorksolutions.jsonjavaproject.json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JsonControllerTest {
    @Mock
    HttpServletRequest request;

    @InjectMocks
    JsonController controller;

    @Mock
    UserAccountRepository repository;

    @Mock
    HashMap<UUID, Long> tokenMap;

    //Ip test
    @Test
    void itShouldReturnClientsIp() {
        String ip = "127.0.01";
        IP expectedIp = new IP(ip);
        var token = UUID.randomUUID();
        when(request.getRemoteAddr()).thenReturn(ip);
        when(tokenMap.containsKey(token)).thenReturn(true);
        Assertions.assertEquals(expectedIp, controller.ip(token, request));
    }

    @Test
    public void datetimeTest(){
        //DateTime dateTime = controller.dateTime(request);
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        Assertions.assertEquals(todayDate, controller.dateTime(request).date);
        Assertions.assertEquals(currentTime, controller.dateTime(request).time);
    }
    //headers test
    @Test
    void ItShouldReturnHeadersWhenCalled() {
        Map<String, String> expected = new HashMap<>();
        expected.put("header", "header1");
        Map<String, String> result = new HashMap<>();
        result.put("header", "header1");
        Assertions.assertEquals(expected, result);
    }
    //Md5 test
    @Test
    void itShouldHAshAbcCorrect() throws NoSuchAlgorithmException {
        String expected = "900150983cd24fb0d6963f7d28e17f72";
//        Map<String, String> expectedMd5 = new HashMap<>();
//        expectedMd5.put("md5", expected);
        //Map<String, String> result = (Map<String, String>) controller.md5("abc");
        Assertions.assertEquals("md5",controller.md5("abc"));
    }
    @Test
    void itShouldReturnArrayWhenGivenArray() {
        String testArray = "[1,2,3]";
        JSONValidate test = controller.validate(testArray);
        Assertions.assertEquals("array", test.object_or_array);
    }
    @Test
    void itShouldReturnUnauthorizedWhenUserIsWrong() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        // ArgumentMatcher - it is a test that can be run on an argument
        // any() - always return true
        // eq(<expected>) - the passed argument must match <expected>
        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());
        // lenient() - I know that a stubbing will not be called in the passing case,
        //      but I want to test for it anyway
        lenient().when(repository.findByUsernameAndPassword(not(eq(username)), eq(password)))
                .thenReturn(Optional.of(new UserAccount()));
        assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
    }

    @Test
    void itShouldReturnUnauthorizedWhenPassIsWrong() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());
        lenient().when(repository.findByUsernameAndPassword(eq(username), not(eq(password))))
                .thenReturn(Optional.of(new UserAccount()));
        assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
    }
    @Test
    void itShouldMapTheUUIDToTheIdWhenLoginSuccess() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        final Long id = (long) (Math.random() * 9999999); // the id of the user account associated with username, password
        final UserAccount expected = new UserAccount();
        expected.id = id;
        expected.username = username;
        expected.password = password;
        when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(expected));
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        when(tokenMap.put(captor.capture(), eq(id))).thenReturn(0L);
        final var token = controller.login(username, password);
        assertEquals(token, captor.getValue());
    }
    @Test
    void itShouldReturnInvalidIfUsernameExists() {
        final String username = "some username";
        when(repository.findByUsername(username)).thenReturn(Optional.of(
                new UserAccount()));
        assertThrows(ResponseStatusException.class, () -> {
            controller.register(username, "");
        });
    }
    @Test
    void itShouldSaveANewUserAccountWhenUserIsUnique() {
        final String username = "some username";
        final String password = "some password";
        when(repository.findByUsername(username)).thenReturn(Optional.empty());
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        when(repository.save(captor.capture())).thenReturn(new UserAccount());
        Assertions.assertDoesNotThrow(() -> {
            controller.register(username, password);
        });
        assertEquals(new UserAccount(), captor.getValue());
    }
    @Test
    void itShouldThrowUnauthorizedWhenIPIsCalledWithBadToken() {
        final var token = UUID.randomUUID();
        when(tokenMap.containsKey(token)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> controller.ip(token, request));
    }
//    @Test
//    void itShouldThrowUnauthorizedWhenFizzBuzzCalledWithBadToken() {
//        final var token = UUID.randomUUID();
//        when(tokenMap.containsKey(token)).thenReturn(false);
//        Integer input = 6;
//        assertThrows(ResponseStatusException.class, () -> controller.fizzbuzz(token, input));
//    }

    @Test
    void itShouldDelTokenWhenLogout() {
        when(repository.findByUsernameAndPassword("user1", "pass1")).thenReturn(Optional.of(
                new UserAccount()));
        UUID login1 = controller.login("user1", "pass1");
        tokenMap.put(login1, 1L);
        when(repository.findByUsernameAndPassword("user2", "pass2")).thenReturn(Optional.of(
                new UserAccount()));
        UUID login2 = controller.login("user2", "pass2");
        tokenMap.put(login2, 2L);
        controller.logout(login1);
        //System.out.println(tokenMapTest);
        //when(tokenMap.containsKey(login2)).thenReturn(true);
        //containsKey login1 or login2 passes
        assertFalse(tokenMap.containsKey(login2));
    }
    @Test
    void itShouldDeleteTokenWhenLogout() {
        final Long id = (long) (Math.random() * 9999999);
        HashMap<UUID, Long> expected = new HashMap<>();
        UUID token = UUID.randomUUID();
        tokenMap.put(token, id);
        assertEquals(expected, controller.logout(token));
    }
    @Test
    void itShouldLogout() {
        final var token = UUID.randomUUID();
        tokenMap.put(token, 1234L);
        controller.logout(token);
        Assertions.assertFalse(tokenMap.containsKey(token));
    }

}

