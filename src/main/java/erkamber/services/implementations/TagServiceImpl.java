package erkamber.services.implementations;

import erkamber.dtos.TagDto;
import erkamber.entities.NewsTag;
import erkamber.entities.Tag;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.TagMapper;
import erkamber.repositories.NewsTagRepository;
import erkamber.repositories.TagRepository;
import erkamber.services.interfaces.TagService;
import erkamber.validations.TagValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    private final TagValidation tagValidation;

    private final NewsTagRepository newsTagRepository;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, TagValidation tagValidation, NewsTagRepository newsTagRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.tagValidation = tagValidation;
        this.newsTagRepository = newsTagRepository;
    }

    /**
     * Adds a new tag using the provided {@link TagDto}.
     *
     * @param newTagDto The {@link TagDto} containing information for the new tag.
     * @return The ID of the newly added tag.
     * @throws InvalidInputException If the provided tag name is incorrect or invalid.
     */
    @Override
    public int addNewTag(TagDto newTagDto) {

        isTagNameCorrect(newTagDto.getTagName());

        Tag newTag = tagMapper.mapTagDtoToTag(newTagDto);

        tagRepository.save(newTag);

        return newTag.getTagID();
    }

    /**
     * Deletes a tag by its tag name.
     *
     * @param tagName The name of the tag to be deleted.
     * @throws InvalidInputException     If the provided tag name is incorrect or invalid.
     * @throws ResourceNotFoundException If the tag with the given name is not found.
     */
    @Override
    public void deleteTagByTagName(String tagName) {

        isTagNameCorrect(tagName);

        Optional<Tag> searchedTag = tagRepository.findTagByTagName(tagName);

        Tag searchedTagObject = searchedTag.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + tagName, "Tag"));

        tagRepository.delete(searchedTagObject);
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param tagID The ID of the tag to be deleted.
     * @throws ResourceNotFoundException If the tag with the given ID is not found.
     */
    @Override
    public void deleteTagByTagID(int tagID) {

        Optional<Tag> searchedTag = tagRepository.findById(tagID);

        Tag searchedTagObject = searchedTag.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + tagID, "Tag"));

        tagRepository.delete(searchedTagObject);
    }

    /**
     * Updates the name of a tag with the specified ID.
     *
     * @param searchedTagID The ID of the tag to be updated.
     * @param updateTagDto  The TagDto object containing the updated tag information.
     * @throws ResourceNotFoundException If the tag with the given ID is not found.
     */
    @Override
    public void updateTag(int searchedTagID, TagDto updateTagDto) {

        Optional<Tag> tagToUpdate = tagRepository.findById(searchedTagID);

        Tag tagWithUpdates = tagToUpdate.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + searchedTagID, "Tag"));

        isTagNameCorrect(updateTagDto.getTagName());

        tagWithUpdates.setTagName(updateTagDto.getTagName());

        tagRepository.save(tagWithUpdates);
    }

    /**
     * Updates the name of a tag with the specified name.
     *
     * @param searchedTagName The current name of the tag to be updated.
     * @param newTagName      The new name to update the tag with.
     * @throws ResourceNotFoundException If the tag with the given current name is not found.
     */
    @Override
    public void updateTagByTagName(String searchedTagName, String newTagName) {

        // Validate the tag names
        isTagNameCorrect(searchedTagName);
        isTagNameCorrect(newTagName);

        // Find the tag by its current name and update its name
        Optional<Tag> tagToUpdate = tagRepository.findTagByTagName(searchedTagName);
        Tag tagWithUpdates = tagToUpdate.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + searchedTagName, "Tag"));

        tagWithUpdates.setTagName(newTagName);

        tagRepository.save(tagWithUpdates);
    }

    /**
     * Finds and retrieves a tag by its ID.
     *
     * @param searchedTagID The ID of the tag to be retrieved.
     * @return The TagDto representation of the found tag.
     * @throws ResourceNotFoundException If the tag with the given ID is not found.
     */
    @Override
    public TagDto findTagByTagID(int searchedTagID) {

        Optional<Tag> searchedTag = tagRepository.findById(searchedTagID);

        Tag tagWithUpdates = searchedTag.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + searchedTagID, "Tag"));

        return tagMapper.mapTagToTagDto(tagWithUpdates);
    }

    /**
     * Finds and retrieves a tag by its name.
     *
     * @param tagName The name of the tag to be retrieved.
     * @return The TagDto representation of the found tag.
     * @throws ResourceNotFoundException If the tag with the given name is not found.
     */
    @Override
    public TagDto findTagByTagName(String tagName) {

        isTagNameCorrect(tagName);

        Optional<Tag> tagToUpdate = tagRepository.findTagByTagName(tagName);

        Tag searchedTag = tagToUpdate.orElseThrow(() ->
                new ResourceNotFoundException("Tag not Found: " + tagName, "Tag"));

        return tagMapper.mapTagToTagDto(searchedTag);
    }

    /**
     * Retrieves a list of all tags.
     *
     * @return A list of TagDto representations of all tags.
     */
    @Override
    public List<TagDto> getAllTags() {

        List<Tag> listOfAllTags = tagRepository.findAll();

        return tagMapper.mapListOfTagToTagDto(listOfAllTags);
    }

    @Override
    public List<TagDto> getTrendingTags() {

        List<NewsTag> listOfAllNewsTagRelations = newsTagRepository.findAll();

        // Step 1: Create a map to count tag occurrences
        Map<Integer, Long> tagCounts = listOfAllNewsTagRelations.stream()
                .collect(Collectors.groupingBy(NewsTag::getTagID, Collectors.counting()));

        // Step 2: Sort the map entries by count in descending order
        List<Map.Entry<Integer, Long>> sortedEntries = tagCounts.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        // Step 3: Extract the sorted tag IDs
        List<Integer> sortedTagIDs = sortedEntries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Step 4: Retrieve the corresponding Tag objects
        List<Tag> sortedTags = sortedTagIDs.stream()
                .map(tagID -> tagRepository.findById(tagID)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not Found: " + tagID, "Tag")))
                .collect(Collectors.toList());

        return tagMapper.mapListOfTagToTagDto(sortedTags);
    }

    /**
     * Checks if the provided tag name is correct and valid.
     *
     * @param tagName The tag name to be validated.
     * @throws InvalidInputException If the provided tag name is invalid.
     */
    private void isTagNameCorrect(String tagName) {

        if (!tagValidation.isTagNameValid(tagName)) {

            throw new InvalidInputException("Invalid Tag. Tag Name must contain only characters");
        }
    }
}
