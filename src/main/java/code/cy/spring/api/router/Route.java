package code.cy.spring.api.router;

import org.springframework.http.ResponseEntity;

public interface Route extends Handler {
    ResponseEntity<?> handler(Request request);
}