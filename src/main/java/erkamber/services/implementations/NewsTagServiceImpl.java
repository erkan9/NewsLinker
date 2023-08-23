package erkamber.services.implementations;

import erkamber.dtos.NewsDetailedDto;
import erkamber.dtos.NewsTagDetailedDto;
import erkamber.dtos.NewsTagDto;
import erkamber.dtos.TagDto;
import erkamber.entities.NewsTag;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.NewsTagMapper;
import erkamber.repositories.NewsTagRepository;
import erkamber.services.interfaces.NewsService;
import erkamber.services.interfaces.NewsTagService;
import erkamber.services.interfaces.TagService;
import erkamber.validations.NewsTagValidation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsTagServiceImpl implements NewsTagService {

    private final NewsTagRepository newsTagRepository;

    private final NewsTagMapper newsTagMapper;

    private final NewsService newsService;

    private final TagService tagService;

    private final NewsTagValidation newsTagValidation;

    public NewsTagServiceImpl(NewsTagRepository newsTagRepository, NewsTagMapper newsTagMapper, NewsService newsService,
                              TagService tagService, NewsTagValidation newsTagValidation) {

        this.newsTagRepository = newsTagRepository;
        this.newsTagMapper = newsTagMapper;
        this.newsService = newsService;
        this.tagService = tagService;
        this.newsTagValidation = newsTagValidation;
    }

    /**
     * Adds a new news tag based on the provided NewsTagDto.
     *
     * @param newsTagDto The NewsTagDto containing information about the new news tag.
     * @return The ID of the newly added news tag.
     */
    @Override
    public int addNewsTag(NewsTagDto newsTagDto) {

        NewsTag newsTag = newsTagMapper.mapNewsTagToNewsTagDto(newsTagDto);

        newsTagRepository.save(newsTag);

        return newsTag.getNewsTagID();
    }

    /**
     * Deletes all news tags associated with a specific news article based on the provided news article ID.
     *
     * @param newsID The ID of the news article whose associated news tags are to be deleted.
     * @return The number of news tags deleted.
     * @throws ResourceNotFoundException If no news tags are found for the specified news article.
     */
    @Override
    public int deleteAllTagsByNewsID(int newsID) {

        List<NewsTag> listOfNewsTagsByNewsID = newsTagRepository.findNewsTagsByNewsID(newsID);

        isNewsTagListEmpty(listOfNewsTagsByNewsID);

        newsTagRepository.deleteAll(listOfNewsTagsByNewsID);

        return listOfNewsTagsByNewsID.size();
    }

    /**
     * Deletes all news tags associated with a specific tag based on the provided tag ID.
     *
     * @param tagID The ID of the tag whose associated news tags are to be deleted.
     * @return The number of news tags deleted.
     * @throws ResourceNotFoundException If no news tags are found for the specified tag.
     */
    @Override
    public int deleteNewsTagByTagID(int tagID) {

        List<NewsTag> listOfNewsTagsByTagID = newsTagRepository.findNewsTagsByTagID(tagID);

        isNewsTagListEmpty(listOfNewsTagsByTagID);

        newsTagRepository.deleteAll(listOfNewsTagsByTagID);

        return listOfNewsTagsByTagID.size();
    }

    /**
     * Deletes a specific news tag associated with a news article based on the provided news article ID and tag ID.
     *
     * @param newsID The ID of the news article from which the news tag is to be deleted.
     * @param tagID  The ID of the tag to be deleted from the news article.
     * @throws ResourceNotFoundException If the specified news tag is not found.
     */
    @Override
    public void deleteNewsTag(int newsID, int tagID) {

        Optional<NewsTag> newsTagOptional = newsTagRepository.findNewsTagsByNewsIDAndTagID(newsID, tagID);

        NewsTag newsTag = newsTagOptional.orElseThrow(() ->
                new ResourceNotFoundException("NewsTag not Found", "NewsTag"));

        newsTagRepository.delete(newsTag);
    }

    /**
     * Retrieves a specific news tag based on the provided news tag ID.
     *
     * @param newsTagID The ID of the news tag to be retrieved.
     * @return A {@link NewsTagDto} object representing the news tag.
     * @throws ResourceNotFoundException If the specified news tag is not found.
     */
    @Override
    public NewsTagDto getNewsTagByID(int newsTagID) {

        Optional<NewsTag> newsTagOptional = newsTagRepository.findById(newsTagID);

        NewsTag newsTag = newsTagOptional.orElseThrow(() ->
                new ResourceNotFoundException("NewsTag not Found", "NewsTag"));

        return newsTagMapper.mapNewsTagToNewsTagDto(newsTag);
    }

    /**
     * Retrieves a list of detailed news tags associated with a specific news article based on the provided news article ID.
     *
     * @param newsID The ID of the news article whose associated news tags are to be retrieved.
     * @return A list of {@link NewsTagDetailedDto} objects representing the detailed news tags associated with the news article.
     * @throws ResourceNotFoundException If no news tags are found for the specified news article.
     */
    @Override
    public List<NewsTagDetailedDto> getTagsOfNewsDetailed(int newsID) {

        List<NewsTag> listOfNewsTagByNewsID = newsTagRepository.findNewsTagsByNewsID(newsID);

        isNewsTagListEmpty(listOfNewsTagByNewsID);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTagByNewsID);
    }

    /**
     * Retrieves a list of news tags associated with a specific news article based on the provided news article ID.
     *
     * @param newsID The ID of the news article whose associated news tags are to be retrieved.
     * @return A list of {@link NewsTagDto} objects representing the news tags associated with the news article.
     * @throws ResourceNotFoundException If no news tags are found for the specified news article.
     */
    @Override
    public List<NewsTagDto> getTagsOfNews(int newsID) {

        List<NewsTag> listOfNewsTagByNewsID = newsTagRepository.findNewsTagsByNewsID(newsID);

        isNewsTagListEmpty(listOfNewsTagByNewsID);

        return newsTagMapper.mapListToNewsTagDto(listOfNewsTagByNewsID);
    }

    /**
     * Retrieves a list of detailed news articles associated with a specific tag based on the provided tag ID.
     *
     * @param tagID The ID of the tag whose associated news articles are to be retrieved.
     * @return A list of {@link NewsTagDetailedDto} objects representing the detailed news articles associated with the tag.
     * @throws ResourceNotFoundException If no news articles are found for the specified tag.
     */
    @Override
    public List<NewsTagDetailedDto> getNewsOfTag(int tagID) {

        List<NewsTag> listOfNewsTagByTagID = newsTagRepository.findNewsTagsByTagID(tagID);

        isNewsTagListEmpty(listOfNewsTagByTagID);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTagByTagID);
    }

    /**
     * Retrieves a list of detailed news tags for all news articles.
     *
     * @return A list of {@link NewsTagDetailedDto} objects representing the detailed news tags for all news articles.
     * @throws ResourceNotFoundException If no news tags are found.
     */
    @Override
    public List<NewsTagDetailedDto> getAllNewsTags() {

        List<NewsTag> listOfNewsTag = newsTagRepository.findAll();

        isNewsTagListEmpty(listOfNewsTag);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTag);
    }

    /**
     * Validates whether a list of news tags is empty and throws an exception if it is.
     *
     * @param newsTagList The list of news tags to be validated.
     * @throws ResourceNotFoundException If the list of news tags is empty.
     */
    private void isNewsTagListEmpty(List<NewsTag> newsTagList) {

        if (newsTagValidation.isListEmpty(newsTagList)) {

            throw new ResourceNotFoundException("Tags for News not Found", "NewsTag");
        }
    }

    /**
     * Converts a list of news tags to a list of detailed news tag DTOs.
     *
     * @param listOfTags The list of news tags to be converted.
     * @return A list of {@link NewsTagDetailedDto} objects representing the detailed news tags.
     */
    private List<NewsTagDetailedDto> convertListToListOfNewsTagDetailedDto(List<NewsTag> listOfTags) {

        List<NewsTagDetailedDto> newsTagDetailedDtoList = new ArrayList<>();

        for (NewsTag newsTag : listOfTags) {

            TagDto tagDto = tagService.findTagByTagID(newsTag.getNewsTagID());

            NewsDetailedDto newsDetailedDto = newsService.getNewsDetailedByNewsID(newsTag.getNewsID());

            NewsTagDetailedDto newsTagDetailedDto1 = newsTagMapper.mapToNewsTagDetailedDto(newsTag.getNewsTagID(), newsDetailedDto, tagDto);

            newsTagDetailedDtoList.add(newsTagDetailedDto1);
        }
        return newsTagDetailedDtoList;
    }
}
