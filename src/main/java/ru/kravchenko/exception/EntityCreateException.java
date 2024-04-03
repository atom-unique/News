package ru.kravchenko.exception;

public class EntityCreateException extends RuntimeException {

    public EntityCreateException(Class<?> clazz) {
        super(clazz.getSimpleName() + " creation error");
    }
}
