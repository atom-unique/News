package ru.kravchenko.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, Object param) {
        super(clazz.getSimpleName() + " not found by parameter: " + param);
    }
}
