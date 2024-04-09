package ru.kravchenko.servlet.mapper;

import ru.kravchenko.model.Tag;
import ru.kravchenko.servlet.dto.IncomingTagDto;
import ru.kravchenko.servlet.dto.OutGoingTagDto;

public interface TagMapper {

    OutGoingTagDto map(Tag source);

    Tag map(IncomingTagDto source);
}
