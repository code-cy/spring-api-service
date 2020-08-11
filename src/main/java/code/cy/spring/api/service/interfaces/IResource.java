package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
import java.util.Map;

import code.cy.spring.api.service.ApiService;


public interface IResource<T extends ApiService<T, ID>, ID extends Serializable>{
    public Map<String, Object> resource(T resource);
}