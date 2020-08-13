package code.cy.spring.api.router;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Request {
    public Map<String, String> pathParams;
    public HttpServletRequest request; 
    public Map<String, String> params; 
    public String body;
    public Request(Map<String, String> pathParams, HttpServletRequest request, Map<String, String> params, String body){
        this.pathParams = pathParams;
        this.request = request;
        this.params = params;
        this.body = body;
    }
}