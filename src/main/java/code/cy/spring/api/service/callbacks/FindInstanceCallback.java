package code.cy.spring.api.service.callbacks;
/**
 * @author Camilo Barbosa
 */
public interface FindInstanceCallback<T> {
    public boolean find(T instance);
}