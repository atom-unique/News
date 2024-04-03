package ru.kravchenko.exception;

public class ExecuteQueryException extends RuntimeException {

    public ExecuteQueryException(Class<?> clazz, Throwable cause) {
        super("Error in SQL query in class: " + clazz.getSimpleName(), cause);
    }
}
