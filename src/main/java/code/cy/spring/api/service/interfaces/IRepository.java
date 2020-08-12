package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
import java.util.Optional;

public interface IRepository<T, ID extends Serializable> {
    public Optional<T> findById(ID id);

    public void deleteById(ID id);

    public Iterable<T> findAll();

    public <S extends T> S save(S entity);

    public void deleteAll();
}