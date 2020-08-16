package code.cy.spring.api.service.callbacks;
/**
 * @author Camilo Barbosa
 */
import org.springframework.http.ResponseEntity;

public interface InstanceCallback<T>{
    public ResponseEntity<?> doSomething(T instance);
}