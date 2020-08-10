package api_service.interfaces;

import java.io.Serializable;
import java.util.Map;

import api_service.Service;


public interface IResource<T extends Service<T, ID>, ID extends Serializable>{
    public Map<String, Object> resource(T resource);
}