package code.cy.spring.api.router;

import java.util.HashMap;
import java.util.Map;

public class RouteMapping {

    public RouteMapping(){
        this.map = new HashMap<Path, Handler>();        
    }
    
    private Map<Path, Handler> map;

    public static RouteMapping create() {       
        return  new RouteMapping();
    }

    public RouteMapping route(Path key, Handler value) {
        map.put(key, value);
        return this;
    }

    public RouteMapping get(String path, Handler value) {
        map.put(Path.get(path), value);
        return this;
    }

    public RouteMapping post(String path, Handler value) {
        map.put(Path.post(path), value);
        return this;
    }

    public RouteMapping delete(String path, Handler value) {
        map.put(Path.delete(path), value);
        return this;
    }

    public RouteMapping put(String path, Handler value) {
        map.put(Path.put(path), value);
        return this;
    }

    public Map<Path, Handler> getMap() {
        return map;
    }    
}