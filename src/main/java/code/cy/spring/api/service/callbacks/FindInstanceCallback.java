package code.cy.spring.api.service.callbacks;

public interface FindInstanceCallback<T> {
    public boolean find(T instance);
}