package code.cy.spring.api.router.interfaces;

import code.cy.spring.api.router.RouteMapping;


public interface IRouter<T> {
    RouteMapping routes(RouteMapping routeMapping);

    Object error404();
    Object error500(Exception e);
}