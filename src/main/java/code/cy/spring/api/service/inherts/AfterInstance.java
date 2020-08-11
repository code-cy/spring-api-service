package code.cy.spring.api.service.inherts;

import org.springframework.http.ResponseEntity;

public interface AfterInstance<T>{
    public ResponseEntity<?> doSomething(T instance);
}