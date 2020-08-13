package code.cy.spring.api.router;

import java.util.Map;

public interface Group extends Handler {
    Map<Path, Handler> routes();
}