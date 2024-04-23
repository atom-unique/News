package ru.kravchenko.servlet.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.kravchenko.model.Tag;
import ru.kravchenko.servlet.dto.CommentDto;
import ru.kravchenko.servlet.dto.IncomingTagDto;
import ru.kravchenko.servlet.dto.OutGoingTagDto;
import ru.kravchenko.servlet.mapper.TagMapper;

import java.util.ArrayList;

class TagMapperImplTest extends CommentDto {

    @Mock
    private TagMapper tagMapper = new TagMapperImpl();

    @Test
    void mapTestModelToDto() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("name");
        tag.setNewsList(new ArrayList<>());
        OutGoingTagDto dto = tagMapper.map(tag);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("name", dto.getName());
        Assertions.assertEquals(new ArrayList<>(), dto.getNewsList());
    }

    @Test
    void mapTestDtoToModel() {
        IncomingTagDto dto = new IncomingTagDto();
        dto.setId(1L);
        dto.setName("name");
        dto.setNewsList(new ArrayList<>());
        Tag tag = tagMapper.map(dto);
        Assertions.assertEquals(1L, tag.getId());
        Assertions.assertEquals("name", tag.getName());
        Assertions.assertEquals(new ArrayList<>(), tag.getNewsList());
    }
}