package erkamber.mappers;

import erkamber.dtos.TagDto;
import erkamber.entities.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagDto mapTagToTagDto(Tag tag) {

        return new TagDto(tag.getTagID(), tag.getTagName());
    }

    public Tag mapTagDtoToTag(TagDto tagDto) {

        return new Tag(tagDto.getTagID(), tagDto.getTagName());
    }

    public List<TagDto> mapListOfTagToTagDto(List<Tag> listOfTag) {

        return listOfTag.stream().map(this::mapTagToTagDto).collect(Collectors.toList());
    }
}
