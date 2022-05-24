package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class Login {
    @JsonProperty("username")
    String username;
    @JsonProperty("password")
    String password;

//    public Login() {
//        var result = repository.findByUsernameAndPassword(username, password);
//
//        // If not found, inform the client that they are unauthorized
//        if (result.isEmpty())
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//
//        // If found:
//        // Generate a token that is to be used for all future requests that are associated
//        //     w/ this user account
//        final var token = UUID.randomUUID();
//        // Associate the generated token w/ the user account
//        tokenMap.put(token, result.get().id);
//        // Provide the generated token to the client for future use
//        return token; // from now on, use this uuid to let me know who you are
//    }
}
