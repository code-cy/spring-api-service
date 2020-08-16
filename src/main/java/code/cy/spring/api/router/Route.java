package code.cy.spring.api.router;
/**
 * @author Camilo Barbosa
 */
import org.springframework.http.ResponseEntity;

public interface Route extends Handler {
    ResponseEntity<?> handler(Request request);
}