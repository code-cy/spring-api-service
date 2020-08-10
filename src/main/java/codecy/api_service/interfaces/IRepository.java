package codecy.api_service.interfaces;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface IRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    public Optional<T> findById(ID id);

    public void deleteById(ID id);
}