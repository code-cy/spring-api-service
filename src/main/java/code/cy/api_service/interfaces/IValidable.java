package code.cy.api_service.interfaces;

import java.util.Map;

public interface IValidable{

    public Map<String, Object> storeRules();

    public Map<String, Object> updateRules();
}