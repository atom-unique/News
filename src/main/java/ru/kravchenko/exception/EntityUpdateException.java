package ru.kravchenko.exception;

public class EntityUpdateException extends RuntimeException {

    public EntityUpdateException(Class<?> clazz) {
        super(clazz.getSimpleName() + " update error");
    }
}
