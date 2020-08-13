package code.cy.spring.api.router;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import code.cy.FastMap;
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
        Map<Path, Handler> routes = router.routes();
        Map<String, String> pathParams = new HashMap<String, String>();

        return findRoute(path, method, routes, pathParams, request, params, body);
    }

    private ResponseEntity<?> findRoute(String path, String method, Map<Path, Handler> routes,
            Map<String, String> pathParams, HttpServletRequest request, Map<String, String> params, String body) {
        for (Path _path : routes.keySet()) {
            String[] path_mapp_split = _path.path.split("\\/");
            String[] path_split = path.split("\\/");
            if (path_mapp_split.length <= path_split.length && method.equals(_path.method)){
                Boolean found = true;
                for (int i = 0; i < path_mapp_split.length; i++) {
                    String value = path_mapp_split[i].trim();
                    if(value.startsWith("{") && value.endsWith("}")){
                        String pathParam = value.replace('{', '\0').replace('}', '\0');
                        pathParams.put(pathParam.trim(),path_split[i]);
                    }else{
                        if(! path_mapp_split[i].equals(path_split[i])){
                            pathParams.clear();
                            found = false;
                            break;
                        }
                    }
                }
                if(found){
                    IMiddleware[] middlewares = _path.middlewares;
                    for(IMiddleware middleware : middlewares){
                        ResponseEntity<?> response = middleware.handler(new Request(pathParams, request, params, body));
                        if(response != null){
                            return response;
                        }
                    }
                }
                
                if(found && path_mapp_split.length == path_split.length){
                    if(routes.get(_path) instanceof Handler){
                        Handler handler = (Handler)routes.get(_path);
                        return handler.handler(new Request(pathParams, request, params, body));                   
                    }
                }
                if(found && path_mapp_split.length < path_split.length){
                    if(routes.get(_path) instanceof Group){
                        Group handler = (Group)routes.get(_path);
                        Map<Path, Handler> g_routes = handler.routes();
                        String g_path = "";
                        for(int i = path_mapp_split.length-1; i<path_split.length;i++){
                            g_path += "/"+path_split[i];
                        }
                        //todo

                    }
                }

                
            }
        }
        return Response.status(404).body(FastMap.get("error", "Page not found."));
    }

}