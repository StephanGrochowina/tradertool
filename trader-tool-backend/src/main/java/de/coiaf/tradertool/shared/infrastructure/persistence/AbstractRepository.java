package de.coiaf.tradertool.shared.infrastructure.persistence;

import de.coiaf.tradertool.shared.domain.AbstractEntity;
import de.coiaf.tradertool.shared.domain.valueobject.BusinessKey;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

/**
 * Base class of all repository classes
 * @param <T> the entity type
 */
public abstract class AbstractRepository<T extends AbstractEntity> {

    @PersistenceContext
    protected EntityManager em;

    /**
     * Determines the class of the entities to be handled by this repository.
     * @return the class of the entities being handled by this repository
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Persists a new entity adding it to the persistence context. The persisting in the database may occur
     * at a later time.
     * Before executing this operation, audit information will be supplied to the shadow table
     * of the entity.
     * @param entity the entity to be persisted
     * @param user the user who invoked this method
     * @return the entity after having been persisted
     */
    public T persist(T entity, String user) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(user);

        updateAudit(entity, user, OperationType.CREATE);
        this.em.persist(entity);
        return entity;
    }

    /**
     * Updates an entity which has already been added to the persistence context. The update into the
     * database may occur at a later time.
     * Before executing this operation, audit information will be supplied to the shadow table
     * of the entity.
     * @param entity the entity to be updated
     * @param user the user who invoked this method
     * @return the entity after having been updated
     */
    public T merge(T entity, String user) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(user);

        updateAudit(entity, user, OperationType.UPDATE);
        return this.em.merge(entity);
    }

    /**
     * Deletes the entity. By default, an UnsupportedOperationException is thrown.
     * Concrete implementation may override that behaviour.
     * @param entity the entity to be deleted
     * @param user the user who invoked this method
     */
    public void delete(T entity, String user) {
        failOnDelete();
    }

    /**
     * Helper method to throw an UnsupportedOperationException.
     */
    protected final void failOnDelete() {
        throw new UnsupportedOperationException("Delete not supported");
    }

    /**
     * Deletes the entity. If the the entity is not attached to the persistence context it
     * will be re-attached first.
     * Before executing this operation, audit information will be supplied to the shadow table
     * of the entity.
     * @param entity the entity to be deleted
     * @param user the user who invoked this method
     */
    protected final void executeHardDelete(T entity, String user) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(user);

        this.updateAudit(entity, user, OperationType.DELETE);
        this.em.remove(this.em.contains(entity) ? entity : this.em.merge(entity));
    }

    /**
     * Finds an entity by its id.
     * @param id the id to lookup
     * @return the entity or null if no entity exists for the provided id
     */
    public T findById(Long id) {
        return this.findById(id, this.getEntityClass());
    }

    /**
     * Finds an entity by its id.
     * @param id the id to lookup
     * @param clazz the class of the entity
     * @return the entity or null if no id exists for the given class
     */
    protected final T findById(Long id, Class<T> clazz) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(clazz);

        return this.em.find(clazz, id);
    }

    /**
     * Finds an entity by its business key.
     * @param businessKey the business key to lookup
     * @return the entity or null if no entity exists for the provided business key
     */
    public T findByBusinessKey(BusinessKey businessKey) {
        return this.findByBusinessKey(businessKey, this.getEntityClass());
    }

    /**
     * Finds an entity by its business key.
     * @param businessKey the business key to lookup
     * @param clazz the class of the entity
     * @return the entity or null if no business key exists for the given class
     */
    protected final T findByBusinessKey(BusinessKey businessKey, Class<T> clazz) {
        Objects.requireNonNull(businessKey);
        Objects.requireNonNull(clazz);

        List<T> result = this.em.createQuery(
                        "SELECT e FROM " + clazz.getSimpleName() + " e WHERE e.businessKey = :bk", clazz)
                .setParameter("bk", businessKey)
                .getResultList();

        if (result.size() > 1) throw new NonUniqueResultException("Business key " + businessKey + " is not unique");

        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Writes data into the shadow table for the entity
     * @param entity the entity to apply an operation on
     * @param user the user applying the operation
     * @param op the operation performed
     */
    protected abstract void updateAudit(T entity, String user, OperationType op);
}
