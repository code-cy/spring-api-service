package code.cy.spring.api.service.interfaces;

import org.springframework.http.ResponseEntity;

public interface IMiddleware {
    ResponseEntity<?> handler();
}