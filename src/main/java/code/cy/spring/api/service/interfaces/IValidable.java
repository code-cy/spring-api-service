package code.cy.spring.api.service.interfaces;
/**
 * @author Camilo Barbosa
 */

import java.util.Map;

public interface IValidable{

    public Map<String, Object> storeRules();

    public Map<String, Object> updateRules();
}