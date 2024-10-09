package com.example.habittracker.repository.inmemory;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseRepositoryInMemory {
    protected static final AtomicInteger globalId = new AtomicInteger(0);
}