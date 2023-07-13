package erkamber.services.interfaces;

import erkamber.dtos.TagDto;

import java.util.List;

public interface TagService {

    int addNewTag(TagDto newTagDto);

    void deleteTagByTagName(String tagName);

    void deleteTagByTagID(int tagID);

    void updateTag(TagDto updateTagDto);

    void updateTagByTagName(String tagName);

    TagDto findTagByTagID(int tagID);

    TagDto findTagByTagName(String tagName);

    List<TagDto> getAllTags();
}
