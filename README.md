# SPRING-API-SERVICE
If you are search to do a **api service fast with spring**, this is your repo.

# Requires
- `java 11`

## Install
 - `git clonde ...`
 - `mvn install`

## Usage

- In your maven package: `pom.xml`
    ```xml
    <dependecies>
        ...
        <dependency>
            <groupId>code-cy</groupId>
            <artifactId>spring-api-service</artifactId>
            <version>1.0.5</version>
        </dependency>
        ...
    </dependecies
    ```

- in your controller:
    ```java
    ...
    import code.cy.spring.api.service.ApiService;
    import code.cy.spring.api.service.Response;

    @RestController
    @RequestMapping('/models/')
    @SpringBootApplication(scanBasePackages={"code.cy"})
    public class ModelController{
        @Autowired
        //the service
        private ApiService<Model, ID> modelService;

        @GetMapping
        //Return a list of Model using CrudRepository.findAll
        public ResponseEntity<?> index(){
            modelService.findInList((Model modelStored)->{
                return modelStored.prop == 10;
            });
            return modelService.list();
        }

        @PostMapping
        //Create a Model in width CrudRepository.save and use Model.storeRules to validation if Model implements IValidable.
        public ResponseEntity<?> store(@RequestBody Model model){
            //Do something after CrudRepository.save and before to send something.
            modelService.doAfterStore((Model modelStored)->{
                modelStored.doSomeMethod();
                //can return null
                return Response.status(201).body(...);
            });
            return modelService.store(model);
        }        

        @GetMapping("/{id}")
        //Show a Model by id in width CrudRepository.findById
        public ResponseEntity<?> show(@PathVariable("id") Long id){        
            return modelService.show(id);
        }

        @PutMapping("/{id}")
        //Update a Model by id and model instance width CrudRepository.save and use Model.updateRules to validation if Model implements IValidable.
        public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Model model){
            //Do something after CrudRepository.save and before to send something.
            modelService.doAfterUpdate((Model modelStored)->{
                modelStored.doOtherMethod();
                //ResponseEntity<?>
                return null;
            });                
            return modelService.update(id, model);
        }

        @DeleteMapping
        //Clear a Model Table in width CrudRepository.deleteAll
        public ResponseEntity<?> clear(){
            return modelService.destoryAll();
        }

        @DeleteMapping("/{id}")
        //Delete a Model by id in width CrudRepository.deleteAll
        public ResponseEntity<?> destory(@PathVariable("id") Long id){
            return modelService.destory(id);
        }
    }
    ```
- In your main class: 
    ```java
    @SpringBootApplication
    class MainClass {...}
    ```
- In your repositories:
    ```java
    import org.springframework.data.repository.CrudRepository;

    import code.cy.spring.api.service.interfaces.IRepository;

    //Is important using the CrudRepository or simililar with IRepository!
    public interface MyModelRepository extends CrudRepository<Model, ID>, IRepository<Model, ID>  {...}  
    ```
- In your model:
    ```java
    ...    
    import code.cy.spring.api.service.interfaces.*;
    import code.cy.spring.api.validation.Rule;
    import code.cy.FastMap;       

    ...    
    class Model implements IModel<Model, ID>{
        ...        
        @Override
        public ID getId() {
            return id;
        }

        @Override
        //Set the Model in ApiService.update.
        public void set(Model data){
            prop1 = data.prop1;
            prop2 = data.prop2;
            ...
        }

        @Override
        //Show the Model to client.
        public Map<String, Object> resource(Model instace){
            return FastMap.create("prop1", instace.prop1)
            .put("prop2", instace.prop2).get();
        }
        ...

    }
    ```
    You can implements: `IValidable` with class: `FastMap`: for creating fast `Map<String, Object>`
    ```java
    ...  

    @Override
        public Map<String, Object> storeRules(){
            return FastMap.makeRule("prop",Rule.UNIQUE).addRule("prop2",Rule.REQUIERED).put("prop3", new String[]{Rule.NO_EXISTS, Rule.OPTIONAL}).get();
        }

    @Override
    public Map<String, Object> updateRules(Model){
        return FastMap.get("prop", new String[]{Rule.UNIQUE});
    }

    
    ...   
    ```

## Changes
- ### `1.0.5`
- Interface: `IModel<Model, ID>`
- Service: `ApiService<Model extends IModel<Model, ID>, ID>`
- deleted: `IResource`
- ### `1.0.4`
- `ApiService`:
    - `void findInList( (Model modelStored)->boolean )`: find modelStoreds in `list()` method.
    - `void doAfterStore( (Model modelStored)->ResponseEntity<?>|null )`: do something after store. if return a response it will be send.
    - `void doAfterUpdate( (Model modelStored)->ResponseEntity<?>|null )`: do something after update. if return a response it will be send.

- ### `1.0.3`
- **General Package Path:** `code.cy.spring.api.service` to `code.cy`.
-  **imports:** 
    ```java
    import code.cy.spring.api.service.ApiService;
    import code.cy.spring.api.service.interfaces.*;
    import code.cy.spring.api.validation.Rule;
    import code.cy.spring.api.validation.Validator;   
    import code.cy.FastMap;     
    ```
- `FastListStr`: *decapritated* 
- `FastMap`:
    - `public static FastMap makeRule(String prop, String rule)`
    - `public FastMap addRule(String prop, String rule)`