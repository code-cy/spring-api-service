package code.cy.spring.api.router.interfaces;
/**
 * @author Camilo Barbosa
 */
import code.cy.spring.api.router.RouteMapping;


public interface IRouter<T> {
    void routes(RouteMapping routeMapping);

    Object error404();
    Object error500(Exception e);
}