package code.cy.spring.api.router.interfaces;
/**
 * @author Camilo Barbosa
 */
import org.springframework.http.ResponseEntity;

import code.cy.spring.api.router.Request;

public interface IMiddleware extends IInternable {
    ResponseEntity<?> handler(Request request);    
}