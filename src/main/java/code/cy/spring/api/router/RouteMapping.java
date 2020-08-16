package code.cy.spring.api.router;
/**
 * @author Camilo Barbosa
 */
import java.util.HashMap;
import java.util.Map;

import code.cy.spring.api.router.interfaces.IMiddleware;

public class RouteMapping {

    public RouteMapping(){
        this.map = new HashMap<Path, Handler>();        
    }
    
    private Map<Path, Handler> map;

    public static RouteMapping create() {       
        return  new RouteMapping();
    }

    public RouteMapping route(Path key, Route value) {
        map.put(key, value);
        return this;
    }

    public RouteMapping get(String path, Route value) {
        map.put(Path.get(path), value);
        return this;
    }

    public RouteMapping get(String path, IMiddleware[] middlewares , Route value) {
        map.put(Path.get(path).middlewares(middlewares), value);
        return this;
    }

    public RouteMapping post(String path, Route value) {
        map.put(Path.post(path), value);
        return this;
    }

    public RouteMapping post(String path, IMiddleware[] middlewares , Route value) {
        map.put(Path.post(path).middlewares(middlewares), value);
        return this;
    }

    public RouteMapping delete(String path, Route value) {
        map.put(Path.delete(path), value);
        return this;
    }

    public RouteMapping delete(String path, IMiddleware[] middlewares , Route value) {
        map.put(Path.delete(path).middlewares(middlewares), value);
        return this;
    }

    public RouteMapping put(String path, Route value) {
        map.put(Path.put(path), value);
        return this;
    }

    public RouteMapping put(String path, IMiddleware[] middlewares , Route value) {
        map.put(Path.put(path).middlewares(middlewares), value);
        return this;
    }

    public RouteMapping group(String path, IMiddleware[] middlewares, Group value) {
        Path _path = Path.get(path).middlewares(middlewares);
        _path.method = "group";
        map.put(_path, value);
        return this;
    }    

    public Map<Path, Handler> getMap() {
        return map;
    }    
}