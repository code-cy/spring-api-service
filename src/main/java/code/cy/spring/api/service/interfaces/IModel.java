package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
import java.util.Map;

import code.cy.spring.api.router.interfaces.IInternable;

public interface IModel<T, ID extends Serializable> extends IInternable {
    public String singular();

    public String plural();

    public ID getId();

    public String getIdName(); 

    public Map<String, Object> resource(T resource);

    public void set(T data);
}