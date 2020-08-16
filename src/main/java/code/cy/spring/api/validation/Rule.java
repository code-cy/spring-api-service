package code.cy.spring.api.validation;
/**
 * @author Camilo Barbosa
 */

public class Rule{
    public static final String REQUIRED = "required";
    public static final String OPTIONAL = "optional";
    public static final String UNIQUE = "unique";  
    public static final String NO_EXISTS = "no-exists";

    public static String rules(String[] rules) {
        String result = "";
        for (String rule : rules) {
            result += rule + "|";
        }
        return result;
    }    
}