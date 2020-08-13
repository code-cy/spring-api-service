package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
import java.util.Map;

public interface IModel<T, ID extends Serializable> {
    public String singular();

    public String plural();

    public ID getId();

    public String getIdName(); 

    public Map<String, Object> resource(T resource);

    public void set(T data);
}