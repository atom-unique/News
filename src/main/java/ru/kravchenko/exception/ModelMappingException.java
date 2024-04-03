package ru.kravchenko.exception;

public class ModelMappingException extends RuntimeException {

    public ModelMappingException(Class<?> clazz) {
        super("Data mapping error for model: " + clazz.getSimpleName());
    }
}
