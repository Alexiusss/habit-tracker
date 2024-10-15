package com.example.habittracker.repository.inmemory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for in-memory repositories that provides shared functionality.
 * This class contains a static {@code AtomicInteger} to generate unique IDs
 * for new entities in the repository.
 *
 * <p>Subclasses can use {@code globalId} to assign incremental IDs to new objects,
 * ensuring each entity has a unique identifier.</p>
 */
public class BaseRepositoryInMemory {

    /**
     * A static counter used to generate unique IDs for entities.
     * This {@code AtomicInteger} is shared across all instances of subclasses
     * and ensures thread-safe ID generation.
     */
    protected static final AtomicInteger globalId = new AtomicInteger(0);
}