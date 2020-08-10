package code.cy.spring.api.service.interfaces;

import java.util.Map;

public interface IValidable{

    public Map<String, Object> storeRules();

    public Map<String, Object> updateRules();
}