package code.cy.api_service;

public class Rule{
    public static final String REQUIRED = "required";
    public static final String OPTIONAL = "optional";
    public static final String UNIQUE = "unique";  

    public static String rules(String[] rules) {
        String result = "";
        for (String rule : rules) {
            result += rule + "|";
        }
        return result;
    }    
}