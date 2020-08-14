package code.cy.spring.api.service.interfaces;

import java.io.Serializable;

import code.cy.spring.api.router.interfaces.IInternable;

public interface IModel<T, ID extends Serializable> extends IInternable {
    public String singular();

    public String plural();

    public ID id();

    public String idName();    

    public void set(T data);
}