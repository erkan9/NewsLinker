package erkamber.services.implementations;

import erkamber.dtos.TagDto;
import erkamber.entities.Tag;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.TagMapper;
import erkamber.repositories.TagRepository;
import erkamber.services.interfaces.TagService;
import erkamber.validations.TagValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    private final TagValidation tagValidation;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, TagValidation tagValidation) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.tagValidation = tagValidation;
    }

    @Override
    public int addNewTag(TagDto newTagDto) {

        isTagNameCorrect(newTagDto.getTagName());

        Tag newTag = tagMapper.mapTagDtoToTag(newTagDto);

        tagRepository.save(newTag);

        return newTag.getTagID();
    }

    @Override
    public void deleteTagByTagName(String tagName) {

        isTagNameCorrect(tagName);

        Optional<Tag> searchedTag = tagRepository.findTagByTagName(tagName);

        Tag searchedTagObject = searchedTag.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + tagName, "Tag"));

        tagRepository.delete(searchedTagObject);

    }

    @Override
    public void deleteTagByTagID(int tagID) {

        Optional<Tag> searchedTag = tagRepository.findById(tagID);

        Tag searchedTagObject = searchedTag.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + tagID, "Tag"));

        tagRepository.delete(searchedTagObject);
    }

    @Override
    public void updateTag(int searchedTagID, TagDto updateTagDto) {

        Optional<Tag> tagToUpdate = tagRepository.findById(searchedTagID);

        Tag tagWithUpdates = tagToUpdate.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + searchedTagID, "Tag"));

        isTagNameCorrect(updateTagDto.getTagName());

        tagWithUpdates.setTagName(updateTagDto.getTagName());

        tagRepository.save(tagWithUpdates);
    }

    @Override
    public void updateTagByTagName(String searchedTagName, String newTagName) {

        isTagNameCorrect(searchedTagName);

        isTagNameCorrect(newTagName);

        Optional<Tag> tagToUpdate = tagRepository.findTagByTagName(searchedTagName);

        Tag tagWithUpdates = tagToUpdate.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + searchedTagName, "Tag"));

        tagWithUpdates.setTagName(newTagName);

        tagRepository.save(tagWithUpdates);
    }

    @Override
    public TagDto findTagByTagID(int searchedTagID) {

        Optional<Tag> searchedTag = tagRepository.findById(searchedTagID);

        Tag tagWithUpdates = searchedTag.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + searchedTagID, "Tag"));

        return tagMapper.mapTagToTagDto(tagWithUpdates);
    }

    @Override
    public TagDto findTagByTagName(String tagName) {

        isTagNameCorrect(tagName);

        Optional<Tag> tagToUpdate = tagRepository.findTagByTagName(tagName);

        Tag searchedTag = tagToUpdate.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + tagName, "Tag"));

        return tagMapper.mapTagToTagDto(searchedTag);
    }

    @Override
    public List<TagDto> getAllTags() {

        List<Tag> listOfAllTags = tagRepository.findAll();

        return tagMapper.mapListOfTagToTagDto(listOfAllTags);
    }

    private void isTagNameCorrect(String tagName) {

        if (!tagValidation.isTagNameValid(tagName)) {

            throw new InvalidInputException("Invalid Tag. Tag Name must contain only characters");
        }
    }
}
