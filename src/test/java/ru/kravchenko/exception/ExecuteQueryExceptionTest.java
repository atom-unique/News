package ru.kravchenko.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.servlet.dto.CommentDto;

class ExecuteQueryExceptionTest extends CommentDto {

    @Test
    void executeQueryExceptionTest() {
        Assertions.assertThrows(ExecuteQueryException.class,
                () -> {
                    throw new ExecuteQueryException(this.getClass(), new Throwable());
                });
    }
}