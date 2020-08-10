package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
import java.util.Map;

import code.cy.spring.api.service.Service;


public interface IResource<T extends Service<T, ID>, ID extends Serializable>{
    public Map<String, Object> resource(T resource);
}