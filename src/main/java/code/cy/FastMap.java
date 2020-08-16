package code.cy;
/**
 * @author Camilo Barbosa
 */
import java.util.HashMap;
import java.util.Map;

public class FastMap{
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
        map.put(key,value);
        return this;
    }

    public Map<String, Object> get(){
        return map;
    }    

    public static FastMap makeRule(String prop, String rule){
        return create(prop,new String[]{rule});
    }    

    public FastMap addRule(String prop,String rule){
        return put(prop,new String[]{rule});
    }

    public static Map<String, Object> get(String key, Object value){
        return create(key, value).get();
    }
    
}