package code.cy.spring.api.router;

import org.springframework.http.ResponseEntity;

public interface Handler {
    ResponseEntity<?> handler(Request request);
}