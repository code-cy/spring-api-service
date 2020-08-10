package code.cy.spring.api.service;

import java.util.HashMap;
import java.util.Map;

public class FastMap {
    public FastMap(){
        this.map = new HashMap<String, Object>();        
    }

    /**
     * @type Map<String, Object>
     */
    private Map<String, Object> map;
    
    public static FastMap create(String key, Object value){
        FastMap fastMap = new FastMap();
        fastMap.map.put(key, value);
        return fastMap;
    }
    
    public FastMap put(String key, Object value){
        map.put(key, value);
        return this;
    }

    public Map<String, Object> get(){
        return map;
    }

    public static Map<String, Object> get(String key, Object value){
        return create(key, value).get();
    }
}