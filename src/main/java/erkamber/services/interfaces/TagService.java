package erkamber.services.interfaces;

import erkamber.dtos.TagDto;

import java.util.List;

public interface TagService {

    int addNewTag(TagDto newTagDto);

    void deleteTagByTagName(String tagName);

    void deleteTagByTagID(int tagID);

    void updateTag(int searchedTagID, TagDto updateTagDto);

    void updateTagByTagName(String searchedTagName, String newTagName);

    TagDto findTagByTagID(int tagID);

    TagDto findTagByTagName(String tagName);

    List<TagDto> getAllTags();
}
