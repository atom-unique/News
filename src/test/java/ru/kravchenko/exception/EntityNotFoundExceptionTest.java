package ru.kravchenko.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.servlet.dto.CommentDto;

class EntityNotFoundExceptionTest extends CommentDto {

    @Test
    void entityNotFoundExceptionTest() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> {
                    throw new EntityNotFoundException(this.getClass(), new Throwable());
                });
    }
}