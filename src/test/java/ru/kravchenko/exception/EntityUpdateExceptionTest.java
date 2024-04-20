package ru.kravchenko.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.servlet.dto.CommentDto;

class EntityUpdateExceptionTest extends CommentDto {

    @Test
    void entityUpdateExceptionTest() {
        Assertions.assertThrows(EntityUpdateException.class,
                () -> {
                    throw new EntityUpdateException(this.getClass());
                });
    }
}