package code.cy.spring.api.router;


public interface Group extends Handler {
    RouteMapping routes(RouteMapping routes);
}