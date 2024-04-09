package ru.kravchenko.servlet.mapper.impl;

import ru.kravchenko.model.Tag;
import ru.kravchenko.servlet.dto.IncomingTagDto;
import ru.kravchenko.servlet.dto.OutGoingTagDto;
import ru.kravchenko.servlet.mapper.TagMapper;

public class TagMapperImpl implements TagMapper {

    @Override
    public OutGoingTagDto map(Tag source) {
        if (source == null) {
            return null;
        }
        OutGoingTagDto target = new OutGoingTagDto();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setNewsList(source.getNewsList());
        return target;
    }

    @Override
    public Tag map(IncomingTagDto source) {
        if (source == null) {
            return null;
        }
        Tag target = new Tag();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setNewsList(source.getNewsList());
        return target;
    }
}
