package code.cy.spring.api.router;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import code.cy.spring.api.router.interfaces.IInternable;

public class Request {
    public Map<String, String> pathParams;
    public HttpServletRequest http; 
    public Map<String, String> params; 
    public String body;
    public Map<String, IInternable> internal;
    public Request(Map<String, String> pathParams, HttpServletRequest request, Map<String, String> params, String body){
        this.pathParams = pathParams;
        this.http = request;        
        this.params = params;
        this.body = body;
        internal = new HashMap<String,IInternable>();
    }

    public <T> T getFromBody(Class<T> clazz){
        Gson gson = new Gson();
        return (T)gson.fromJson(body, clazz);
    }

    public <T> T getFromParam(String param, Class<T> clazz){
        Gson gson = new Gson();
        return (T)gson.fromJson(params.get(param), clazz);
    }
}