# SPRING-API-SERVICE

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
            <version>1.0.2</version>
        </dependency>
        ...
    </dependecies
    ```
- In your main class: 
    ```java
    @SpringBootApplication(scanBasePackages={"code.cy.spring.api.service"})
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
    import code.cy.spring.api.service.Service;
    import code.cy.spring.api.service.interfaces.*;

    @Repository
    class Model extends Service<Model, ID>{
        public ID id,
        @Override
        public ID getId() {
            return id;
        }
    }
    ```
    You can implements: `IValidable`, `IResource<Model, ID>` with clases: `FastMap`, `FastListStr`
    ```java
    ...
    @Override
    public Map<String, Object> storeRules(){
        return FastMap.get("prop", FastListStr.create(Rule.UNIQUE).add(Rule.REQUIERED).rules());
    }

    @Override
    public Map<String, Object> updateRules(Model){
        return FastMap.get("prop", FastListStr.create(Rule.UNIQUE).rules());
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
    @RestController
    @RequestMapping('/services/')
    public class ServiceController{
        @Autowired
        private Model modelService;

        @GetMapping
        public ResponseEntity<?> index(){
            return modelService.list();
        }

        @PostMapping
        public ResponseEntity<?> store(@RequestBody Model model){
            return modelService.store(model);
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> store(@RequestBody Model model){
            return modelService.store(model);
        }

         @GetMapping("/{id}")
        public ResponseEntity<?> show(@PathVariable("id") Long id){        
            return modelService.show(id);
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Model model){                
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

