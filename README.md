# SPRING-API-SERVICE

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
            <version>1.0.4</version>
        </dependency>
        ...
    </dependecies
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

    public interface MyModelRepository extends CrudRepository<Model, ID>, IRepository<Model, ID>  {...}  
    ```
- In your model:
    ```java
    ...
    import code.cy.spring.api.service.ApiService;
    import code.cy.spring.api.service.interfaces.*;
    import code.cy.spring.api.validation.Rule;
    import code.cy.FastMap;       

    @Repository
    class Model extends ApiService<Model, ID>{
        public ID id;

        @Override
        public ID getId() {
            return id;
        }

        @Override
        public void set(Model data){
            prop1 = data.prop1;
            prop2 = data.prop2;
            ...
        }

    }
    ```
    You can implements: `IValidable`, `IResource<Model, ID>` with clases: `FastMap`: for creating fast `Map<String, Object>`
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

    @Override
    public Map<String, Object> resource(Model instace){
        return FastMap.create("prop1", instace.prop1)
        .put("prop2", instace.prop2).get();
    } 
    ...   
    ```
- in your controller:
    ```java
    import code.cy.spring.api.service.Response;

    @RestController
    @RequestMapping('/services/')
    @SpringBootApplication(scanBasePackages={"code.cy"})
    public class ServiceController{
        @Autowired
        private Model modelService;

        @GetMapping
        public ResponseEntity<?> index(){
            modelService.findInList((Model intance)->{
                return intance.prop == 10;
            })
            return modelService.list();
        }

        @PostMapping
        public ResponseEntity<?> store(@RequestBody Model model){
            modelService.doAfterStore((Model intance)->{
                intance.doSomeMethod();
                //can return null
                return Response.status(201).body(...);
            })
            return modelService.store(model);
        }        

         @GetMapping("/{id}")
        public ResponseEntity<?> show(@PathVariable("id") Long id){        
            return modelService.show(id);
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Model model){
            modelService.doAfterUpdate((Model intance)->{
                intance.doOtherMethod();
                //ResponseEntity<?>
                return null;
            })                
            return modelService.update(id, model);
        }

        @DeleteMapping
        public ResponseEntity<?> clear(){
            return modelService.destoryAll();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> destory(@PathVariable("id") Long id){
            return modelService.destory(id);
        }
    }
    ```
## Changes
- ### `1.0.4`
- `ApiService`:
    - `void findInList( (Model intance)->boolean )`: find intances in `list()` method.
    - `void doAfterStore( (Model intance)->ResponseEntity<?>|null )`: do something after store. if return a response it will be send.
    - `void doAfterUpdate( (Model intance)->ResponseEntity<?>|null )`: do something after update. if return a response it will be send.

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