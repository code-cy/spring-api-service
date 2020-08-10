package code.cy.spring.api.service.interfaces;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    public Optional<T> findById(ID id);

    public void deleteById(ID id);
}