package code.cy.spring.api.service.interfaces;
/**
 * @author Camilo Barbosa
 */

import org.springframework.http.ResponseEntity;

public interface IMiddleware {
    ResponseEntity<?> handler();    
}