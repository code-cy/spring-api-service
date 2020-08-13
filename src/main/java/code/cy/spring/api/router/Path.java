package code.cy.spring.api.router;

public class Path {

    public static class HttpMethod {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
    }

    public String path;
    public String method;

    public Path(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static Path get(String path) {
        return new Path(path, HttpMethod.GET);
    }

    public static Path post(String path) {
        return new Path(path, HttpMethod.POST);
    }

    public static Path put(String path) {
        return new Path(path, HttpMethod.PUT);
    }

    public static Path delete(String path) {
        return new Path(path, HttpMethod.DELETE);
    }
}