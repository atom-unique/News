package ru.kravchenko.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.servlet.dto.CommentDto;

class EntityCreateExceptionTest extends CommentDto {

    @Test
    void entityCreateExceptionTest() {
        Assertions.assertThrows(EntityCreateException.class,
                () -> {
                    throw new EntityCreateException(this.getClass());
                });
    }
}