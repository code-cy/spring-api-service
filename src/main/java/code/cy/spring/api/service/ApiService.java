package code.cy.spring.api.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import code.cy.FastMap;
import code.cy.spring.api.service.callbacks.InstanceCallback;
import code.cy.spring.api.service.callbacks.CallbackGetResponseEntity;
import code.cy.spring.api.service.callbacks.FindInstanceCallback;
import code.cy.spring.api.service.interfaces.IHashId;
import code.cy.spring.api.service.interfaces.IMiddleware;
import code.cy.spring.api.service.interfaces.IModel;
import code.cy.spring.api.service.interfaces.IRepository;
import code.cy.spring.api.service.interfaces.IValidable;
import code.cy.spring.api.validation.Validator;

@Repository
public class ApiService<T extends IModel<T, ID>, ID extends Serializable> {

    @Autowired
    private IRepository<T, ID> repository;

    @Autowired
    private Validator<T, ID> validator;

    @Autowired
    private IModel<T, ID> model;

    @Autowired(required = false)
    private IHashId<T, ID> hashId;

    private InstanceCallback<T> afterStore;
    private InstanceCallback<T> afterUpdate;
    private FindInstanceCallback<T> findInstance;

    public void doAfterStore(InstanceCallback<T> after) {
        afterStore = after;
    }

    public void doAfterUpdate(InstanceCallback<T> after) {
        afterUpdate = after;
    }

    public void findInList(FindInstanceCallback<T> find) {
        findInstance = find;
    }

    public IRepository<T, ID> getRepository() {
        return repository;
    }

    public ResponseEntity<?> handler(IMiddleware[] middlewares, CallbackGetResponseEntity callback){
        for(IMiddleware middleware : middlewares){
            ResponseEntity<?> response = middleware.handler();
            if(response != null)
                return response;
        }
        return callback.doSomething();
    }

    public T getByHashId(String eId) {
        ID id = hashId.decode(eId);
        Optional<T> result = repository.findById(id);
        if (result.isPresent())
            return result.get();
        return null;
    }

    public ResponseEntity<?> store(T instance) {
        IRepository<T, ID> service = getRepository();
        IValidable valid = (IValidable) instance;
        if (instance != null) {
            ResponseEntity<?> invalid;
            invalid = _validation(instance, valid.storeRules(), null);
            if (invalid != null)
                return invalid;
        }
        service.save(instance);
        if (afterStore != null) {
            ResponseEntity<?> after = afterStore.doSomething(instance);
            if (after != null)
                return after;
        }
        Map<String, Object> resource = resource(instance);
        if (resource != null)
            return Response.status(201).body(resource);
        return Response.status(201).body(FastMap.get(model.singular(), instance));
    }

    public ResponseEntity<?> update(ID id, T data) {
        IRepository<T, ID> service = getRepository();
        Optional<T> found = service.findById(id);
        if (!found.isPresent())
            return Response.status(404).body(FastMap.get("error", "Recurso no encontrado!"));
        T instance = found.get();
        IValidable valid = (IValidable) instance;
        if (instance != null) {
            ResponseEntity<?> invalid;
            invalid = _validation(instance, valid.updateRules(), id);
            if (invalid != null)
                return invalid;
        }
        instance.set(data);
        service.save(instance);
        if (afterUpdate != null) {
            ResponseEntity<?> after = afterUpdate.doSomething(instance);
            if (after != null)
                return after;
        }

        Map<String, Object> resource = resource(instance);
        if (resource != null)
            return Response.status(200).body(resource);
        return Response.status(200).body(FastMap.get(model.singular(), instance));

    }

    public ResponseEntity<?> list() {
        IRepository<T, ID> service = getRepository();
        Iterable<T> all_iter = service.findAll();
        ArrayList<Object> result = new ArrayList<Object>();
        for (T instance : all_iter) {
            Map<String, Object> resource = null;
            if (findInstance != null) {
                if (findInstance.find(instance)) 
                    resource = instance.resource(instance);
            } else resource = instance.resource(instance);
            if (resource != null) {
                hashId(instance, resource);
                result.add(resource);
            }
        }
        return Response.status(200).body(FastMap.get(model.plural(), result));
    }

    public ResponseEntity<?> show(ID id) {
        IRepository<T, ID> service = getRepository();
        Optional<T> found = service.findById(id);
        if (!found.isPresent())
            return _resourceNotFoundResponse();
        T instance = found.get();
        Map<String, Object> resource = resource(instance);
        if (resource != null)
            return Response.status(200).body(resource);
        return Response.status(200).body(FastMap.get(model.singular(), instance));
    }

    public ResponseEntity<?> destory(ID id) {
        IRepository<T, ID> service = getRepository();
        Optional<T> found = service.findById(id);
        if (!found.isPresent())
            return _resourceNotFoundResponse();
        service.deleteById(id);
        return Response.status(200).body(FastMap.get(model.singular(), null));
    }

    public ResponseEntity<?> destoryAll() {
        IRepository<T, ID> service = getRepository();
        service.deleteAll();
        return Response.status(200).body(FastMap.get(model.plural(), service.findAll()));
    }

    protected ResponseEntity<?> _resourceNotFoundResponse() {
        return Response.status(404).body(FastMap.get("error", "Recurso no encontrado!"));
    }

    private ResponseEntity<?> _validation(T data, Map<String, Object> rules, ID id) {
        try {
            return validator.validate(data, rules, id);
        } catch (Exception err) {
            return Response.status(500).body(FastMap.get("error", err.getMessage()));
        }
    }

    public Map<String, Object> resource(T instance) {
        Map<String, Object> resource = instance.resource(instance);
        if (resource != null) {
            hashId(instance, resource);
            return FastMap.get(model.singular(), resource);
        }
        return null;
    }

    private void hashId(T instance, Map<String, Object> resource) {
        if (hashId != null) {
            resource.put(model.getIdName(), hashId.encode(instance.getId()));
        }
    }

}