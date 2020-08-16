# SPRING-API-SERVICE
If you are search to do a **api service fast with spring**, this is your repo.

# Tags
- [SPIRNG-API-SERVICE](#in-your-controller)
    - [Middleware](#middlewares) 
    - [HashId Connector](#hashid-connector)
- [SPRING-API-ROUTER](#spring-api-router)
    - [Middleware](#router-middleware)
# Autors
 - [Camilo Barbosa](https://github.com/calimpio)

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
            <version>1.1.2</version>
        </dependency>
        ...
    </dependecies
    ```

- ### in your controller:
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
        //Return a list of Model using IRepository.findAll
        public ResponseEntity<?> index(){
            modelService.findInList((Model modelStored)->{
                return modelStored.prop == 10;
            });
            return modelService.list();
        }

        @PostMapping
        //Create a Model in width IRepository.save and use Model.storeRules to validation if Model implements IValidable.
        public ResponseEntity<?> store(@RequestBody Model model){
            //Do something after IRepository.save and before to send something.
            modelService.doAfterStore((Model modelStored)->{
                modelStored.doSomeMethod();
                //can return null
                return Response.status(201).body(...);
            });
            return modelService.store(model);
        }        

        @GetMapping("/{id}")
        //Show a Model by id in width IRepository.findById
        public ResponseEntity<?> show(@PathVariable("id") Long id){        
            return modelService.show(id);
        }

        @PutMapping("/{id}")
        //Update a Model by id and model instance width IRepository.save and use Model.updateRules to validation if Model implements IValidable.
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
        //Clear a Model Table in width IRepository.deleteAll
        public ResponseEntity<?> clear(){
            return modelService.destoryAll();
        }

        @DeleteMapping("/{id}")
        //Delete a Model by id in width IRepository.deleteAll
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
        public ID id() {
            return id;
        }

        @Override
        public String idName() {
            return "id";
        }

        @Override
        //Set the Model in ApiService.update.
        public void set(Model data){
            prop1 = data.prop1;
            prop2 = data.prop2;
            ...
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
- ### Middlewares
    The middlewares are classes to do something before the client petittion go to important things but can be integarte in many of routes. To create `Middleware` class we need `IMiddleware`.
    An example of `IMiddleware` is when we have a `service1/{model1_id}/service2/{model2_id}/...` or when we wan to `authenticate`.

    - In your middlewares:
    ```java
    import code.cy.spring.api.service.interfaces.IMiddleware;
    
    @Repository
    class ModelServiceMiddleware implements IMiddleware{
        @Autowired
        private ApiService<Model, ID> service;
        private Model model;

        //custom information from controller
        public void request(String strId){
            model = service.getByHashId(strId);
        }

        public Model getModel(){
            return model;
        }

        @Override
        public ResponseEntity<?> handler(){
            if(model==null){
                return Response.status(404).body(FastMap.get("error","resource not found"));
            }
            //send null to continue in the controller method.
            return null;
        }

    }
    ```
    - Usage in your controller or service:
    ```java
        ...
        @Autowired        
        private ApiService<Model2, ID> model2service;
        @Autowired
        private  MyMiddleware mymiddleware;

        @GetMapping
        public ResponseEntity<?> modelItems(@PathVariable("model_id") String id){
            
            mymiddleware.request(id);

            return model2service.handler(new IMiddleware[]{mymiddleware, ... },()->{

                return Response.status(200).body(mymiddleware.getModel().getItems());

            });
        }
        ...
    ```
- ### HashId Connector
   Using this feacture is to hide the resources ids to clinet.
   We can use `HashId<Model, ID>` to `encode` and `decode` the ids.

   - In your model:
    ```java
    ...    
    import code.cy.spring.api.service.interfaces.*;
    ...    
    class Model implements IModel<Model, ID>, IHashId<Model, ID>{
        ...        
        @Override public String encode(ID id){
            //encode connector
        }

        @Override public ID decode(String strId){
            //decode connector
        }
        ...
    }
    ```
    - Usage: 
        - `Model ApiService<Model, ID> service.getByHashId(String strId)` to get a Model instance.
        - `Map<String, Object> ApiService<Model, ID> service.resource(Model instance)` to send a hashed id to client.

# SPRING-API-ROUTER
Is a custom implementation using spring as boot. This implementation is good to separete the controllers to http routes logic.

- Usage:
    - In your routes:
    ```java
    ...    
    import code.cy.spring.api.router.Request;
    import code.cy.spring.api.router.RouteMapping;
    import code.cy.spring.api.router.interfaces.IRouter;
    import code.cy.spring.api.service.Response;

    @Repository
    public class Routes implements IRouter<Routes> {
        @Autowired
        private UserController userController;

        @Override
        public RouteMapping routes(RouteMapping route) {

            route.get("/helloWorld", (Request request)->{
                return Response.status(200).body("hello world");
            });

            route.group("/users", null, (RouteMapping uruote) -> {
                uruote.get("", userController::index);
                
                uruote.post("", userController::store);
                
                return uruote;
            });

            return route;
        }

        @Override
        public Object error404() {
            return FastMap.get("error", "not found.");
        }

        @Override
        public Object error500(Exception e) {
            System.out.println(e.message());
            return FastMap.get("error", "internal error.");
        }

    }
    ```
    - in your `main` controller:
    ```java
    @RestController
    @SpringBootApplication(scanBasePackages={"code.cy"})
    public class RouterConfig {
        
        @Autowired
        public Router<Routes> routes;

        @RequestMapping("/**")
        public ResponseEntity<?> handler(HttpServletRequest request, @RequestParam Map<String, String> params, @RequestBody String body){
            return routes.handler(request, params, body);        
        }   
    }
    ```
    - in your controller:
    ```java
    import code.cy.spring.api.router.Request;
    import code.cy.spring.api.service.ApiService;

    @Repository
    public class UserController {
        @Autowired
        private ApiService<User, Long> apiService;

        public ResponseEntity<?> index(Request request ){
            return apiService.list();
        }

        public ResponseEntity<?> store(Request request ){
            //get param in json to class
            Model model = request.getFromParam("model", Model.class);

            return apiService.store(request.getFromBody(User.class));
        }
    }
    ```
    - #### Router-Middleware
    ```java
    ...
    import code.cy.spring.api.router.interfaces.IMiddleware;

    @Repository
    public class UserMiddleware implements IMiddleware {

        @Autowired
        private ApiService<User, Long> userService;
        private Optional<User> user;

        public String user_param;

        public UserMiddleware setParam(String param){
            user_param = param;
            return this;
        }

        @Override
        public ResponseEntity<?> handler(Request request) {
            String strId = request.pathParams.get(user_param);
            if (strId != null) {
                user = userService.getRepository().findById(Long.parseLong(strId));
                if (user.isPresent()){
                    request.internal.put(user_param, user.get());
                    // return null to go to route controller.
                    return null;
                }                
            }
            return Response.status(404).body(FastMap.get("error", "User not found."));
        }

    }
    ```
    - Usage:
        -  in your routes:
        ```java
        route.group("/{user_id}", 
        new IMiddleware[]{userMiddleware.setParam("user_id")},
        (RouteMapping urr)->{
            urr.get("/", userController::show);
            urr.put("/", userController::update);
            return urr;
        });
        ```
        - We can use a `Map<String, IInternable> Request.internal` to send data from middleware to controlles:
        ```java        
        ...
        @Controller
        class UserController{
            @Autowired
            private ApiService<User, Long> userService;

            ...

            public ResponseEntity<?> show(Request request ){
                User user = (User)request.internal.get("user_id");
                return Response.status(200).body(user);
            }

            public ResponseEntity<?> update(Request request){
                User user = (User)request.internal.get("user_id");
                User data = request.getFromBody(User.class);
                return userService.update(user, data);
            }
            ...
        }
        ...
        ```




## Changes
- ### `1.1.2`
    - Method: `ApiService.update(Model stored, Model data)`
- ### `1.1.1`
    - Decapritated: `IModel.resource(Model instance)`
    - Decapritated: `ApiService.resource(Model instance)`
- ### `1.1.0`
    - `SPRING-API-ROUTER`
- ### `1.0.7`
    - Interface: `IMiddleware`
    - Method: `ResponseEntity<?> ApiService.handler(IMiddlewares[] middlewares, ()->ResponseEntity<?>)`
    - Interface: `IHashId<Model, ID>`
    - Method: `Model ApiService<Model, ID> service.getByHashId(String strId)`
    - Method: `Map<String, Object> ApiService<Model, ID> service.resource(Model instance)`

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
