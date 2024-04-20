package ru.kravchenko.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.servlet.dto.CommentDto;

class ModelMappingExceptionTest extends CommentDto {

    @Test
    void modelMappingExceptionTest() {
        Assertions.assertThrows(ModelMappingException.class,
                () -> {
                    throw new ModelMappingException(this.getClass());
                });
    }
}