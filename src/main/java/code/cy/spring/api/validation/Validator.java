package code.cy.spring.api.validation;
/**
 * @author Camilo Barbosa
 */

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import code.cy.spring.api.service.Response;
import code.cy.spring.api.service.interfaces.IModel;
import code.cy.spring.api.service.interfaces.IRepository;

@Repository
public class Validator<T extends IModel<T, ID>, ID extends Serializable> {

    @Autowired
    private IRepository<T, ID> repository;

    public ResponseEntity<Map<String, Object>> validate(T data, Map<String, Object> rules, ID id) throws Exception {
        Map<String, Object> invalid = new HashMap<String, Object>();        
        for (String rule_key : rules.keySet()) {
            Object rolelist = rules.get(rule_key);
            Object[] rule_split = (Object[]) rolelist;                                  
            for (Object rule_split_key : rule_split) {                
                _uniqueValidation(data, invalid, rule_key, (String)rule_split_key, id);
            }
        }
        if (!invalid.isEmpty()) {
            return Response.status(422).body(invalid);
        }
        return null;
    }

    private void _uniqueValidation(T data, Map<String, Object> invalid, String key, String rule, ID id)
            throws Exception {
        if ((rule.equals(Rule.UNIQUE))) {
            boolean noExist = true;
            Iterable<T> iter = repository.findAll();
            for (T instance : iter) {
                Field field = instance.getClass().getField(key);                
                Field dataf = data.getClass().getField(key);
                boolean isNotUnique = dataf.get(data).equals(field.get(instance));                
                if (isNotUnique) {                   
                    if(!(instance.id().equals(id))){                        
                        noExist = false;
                        break;                        
                    }                                    
                }
            }
            if (!noExist)
                invalid.put(key, "El campo ya fue tomado.");
        }
    }
}