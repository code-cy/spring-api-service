package code.cy.spring.api.router.interfaces;

import org.springframework.http.ResponseEntity;

import code.cy.spring.api.router.Request;

public interface IMiddleware {
    ResponseEntity<?> handler(Request request);    
}