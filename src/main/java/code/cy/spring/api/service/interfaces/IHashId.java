package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
/**
 * @author Camilo Barbosa
 */

public interface IHashId<T extends IModel<T, ID>, ID extends Serializable>{
    ID decode(String id);
    String encode(ID id);
}