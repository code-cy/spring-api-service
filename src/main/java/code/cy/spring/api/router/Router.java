package code.cy.spring.api.router;
/**
 * @author Camilo Barbosa
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import code.cy.spring.api.router.interfaces.IMiddleware;
import code.cy.spring.api.router.interfaces.IRouter;
import code.cy.spring.api.service.Response;

@Repository
public class Router<T> {

    @Autowired
    private IRouter<T> router;

    public ResponseEntity<?> handler(HttpServletRequest request, Map<String, String> params, String body) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        Map<Path, Handler> routes = router.routes(RouteMapping.create()).getMap();
        Map<String, String> pathParams = new HashMap<String, String>();
        try {
            return findRoute(path, method, routes, new Request(pathParams, request, params, body));
        } catch (Exception e) {
            return Response.status(500).body(router.error500(e));
        }
    }

    private ResponseEntity<?> findRoute(String path, String method, Map<Path, Handler> routes, Request request)
            throws Exception {
        for (Path _path : routes.keySet()) {            
            String[] path_mapp_split = _path.path.split("\\/");
            String[] path_split = path.split("\\/");
            if (path_mapp_split.length <= path_split.length && (method.equals(_path.method) || _path.method.equals("group"))) {                
                Boolean found = true;
                ArrayList<String> params = new ArrayList<String>();
                for (int i = 0; i < path_mapp_split.length; i++) {
                    String value = path_mapp_split[i].trim();
                    if (value.startsWith("{") && value.endsWith("}")) {
                        String pathParam = value.replace('{', '\0').replace('}', '\0').trim();
                        request.pathParams.put(pathParam, path_split[i]);
                        params.add(pathParam);
                    } else {
                        if (!path_mapp_split[i].equals(path_split[i])) {
                            for (String param : params) {
                                request.pathParams.remove(param);
                            }
                            found = false;
                            break;
                        }
                    }
                }
                if (found) {
                    IMiddleware[] middlewares = _path.middlewares;
                    if (middlewares != null)
                        for (IMiddleware middleware : middlewares) {
                            ResponseEntity<?> response = middleware.handler(request);
                            if (response != null) {
                                return response;
                            }
                        }
                }

                if (found && path_mapp_split.length == path_split.length) {
                    if (routes.get(_path) instanceof Route) {
                        System.out.println("["+method+"] "+_path.path);
                        Route handler = (Route) routes.get(_path);
                        return handler.handler(request);
                    }
                }
                if (found) {
                    if (routes.get(_path) instanceof Group) {
                        Group handler = (Group) routes.get(_path);
                        Map<Path, Handler> m_routes = handler.routes(RouteMapping.create()).getMap();                    
                        for (Path m_path : m_routes.keySet()) {
                            m_path.path = _path.path + m_path.path;
                        }
                        return this.findRoute(path, method, m_routes, request);
                    }
                }

            }
        }
        return Response.status(404).body(router.error404());
    }

}