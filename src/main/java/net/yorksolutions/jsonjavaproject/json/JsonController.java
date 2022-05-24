package net.yorksolutions.jsonjavaproject.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanProperty;
import java.beans.JavaBean;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class JsonController {

    private final UserAccountRepository repository;
    private final HashMap<UUID, Long> tokenMap;

    // This is for Spring
    @Autowired
    public JsonController(@NonNull UserAccountRepository repository) {
        this.repository = repository;
        this.tokenMap = new HashMap<>();
    }

    // This is for Mockito
    public JsonController(@NonNull UserAccountRepository repository, @NonNull HashMap<UUID, Long> tokenMap) {
        this.repository = repository;
        this.tokenMap = tokenMap;
    }

    @GetMapping("/ip")
    @CrossOrigin

    public IP ip(UUID token, HttpServletRequest request) {
        checkAuthorized(token);
//        if (!tokenMap.containsKey(token))
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return new IP(request.getRemoteAddr());
    }

    @GetMapping("/datetime")
    @CrossOrigin
    public DateTime dateTime(UUID token, HttpServletRequest request) {
        checkAuthorized(token);
        return new DateTime();
    }

    @GetMapping("/headers")
    @CrossOrigin
    public Map<String, String> headers(UUID token, @RequestHeader Map<String, String> headers) {
        checkAuthorized(token);
        return headers;
    }

    @GetMapping("/md5")
    @CrossOrigin
    public Md5 md5(UUID token, @RequestParam String text) throws NoSuchAlgorithmException {
        checkAuthorized(token);

        return new Md5(text);
    }

    @GetMapping("/validate")
    @CrossOrigin
    public JSONValidate validate(UUID token, @RequestParam String json) {
        checkAuthorized(token);
        return new JSONValidate(json);
    }

    @GetMapping("/login")
    @CrossOrigin
    UUID login(@RequestParam String username, @RequestParam String password) {
        // check to see if the username exists
        // Search for a UserAccount with the given username and password
        var result = repository.findByUsernameAndPassword(username, password);

        // If not found, inform the client that they are unauthorized
        if (result.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        else if (repository.findByUsername(username).isPresent())
        throw new ResponseStatusException(HttpStatus.CONFLICT);

        // If found:
        // Generate a token that is to be used for all future requests that are associated
        //     w/ this user account
        final var token = UUID.randomUUID();
        // Associate the generated token w/ the user account
        tokenMap.put(token, result.get().id);
        // Provide the generated token to the client for future use
        return token; // from now on, use this uuid to let me know who you are
    }

    @GetMapping("/register")
    @CrossOrigin
    public void register(@RequestParam String username, @RequestParam String password) {
        if (repository.findByUsername(username).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        UserAccount newUser = new UserAccount(username, password);
        repository.save(newUser);
    }

    @GetMapping("/logout")
    @CrossOrigin
    public HashMap<UUID, Long> logout(@RequestParam UUID token) {
        Iterator<Map.Entry<UUID, Long>> iterator = tokenMap.entrySet().iterator();
        while(iterator.hasNext()) {
            if(iterator.next().getKey().equals(token)) {
                iterator.remove();
            }
        }
        return tokenMap;
    }

    public void checkAuthorized(UUID token) {
    }
}
