package net.yorksolutions.jsonjavaproject.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AuthorizeController {



    private final UserAccountRepository repository;
    private final HashMap<UUID, Long> tokenMap;

    @Autowired
    public AuthorizeController(@NonNull UserAccountRepository repository) {
        this.repository = repository;
        this.tokenMap = new HashMap<>();
    }
    public AuthorizeController(@NonNull UserAccountRepository repository, @NonNull HashMap<UUID, Long> tokenMap) {
        this.repository = repository;
        this.tokenMap = tokenMap;
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
//        else if (tokenMap.containsValue(result.get().getId())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT);
//        }
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
    public void logout(UUID token) {
        tokenMap.remove(token);
        }
}
