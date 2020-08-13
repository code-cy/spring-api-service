package code.cy.spring.api.router.interfaces;

import java.util.Map;

import code.cy.spring.api.router.Handler;
import code.cy.spring.api.router.Path;


public interface IRouter<T> {
    Map<Path, Handler> routes();

    Object error404();
}