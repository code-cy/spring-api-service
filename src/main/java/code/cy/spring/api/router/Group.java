package code.cy.spring.api.router;
/**
 * @author Camilo Barbosa
 */

public interface Group extends Handler {
    void routes(RouteMapping routes);
}