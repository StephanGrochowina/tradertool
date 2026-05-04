package de.coiaf.tradertool.shared.domain;

/**
 * Entity which is marked as deleted but which is never physically deleted.
 */
public interface SoftDeletable {
    /**
     * Gets if this entity is marked as deleted
     * @return true if the entity is marked as deleted
     */
    boolean isDeleted();

    /**
     * Marks this entity as deleted
     * @param deleted the flag to indicate whether the entity should be marked as deleted.
     */
    void setDeleted(boolean deleted);
}
