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

    @Override
    public int addNewsTag(NewsTagDto newsTagDto) {

        NewsTag newsTag = newsTagMapper.mapNewsTagToNewsTagDto(newsTagDto);

        newsTagRepository.save(newsTag);

        return newsTag.getNewsTagID();
    }

    @Override
    public int deleteAllTagsByNewsID(int newsID) {

        List<NewsTag> listOfNewsTagsByNewsID = newsTagRepository.findNewsTagsByNewsID(newsID);

        isNewsTagListEmpty(listOfNewsTagsByNewsID);

        newsTagRepository.deleteAll(listOfNewsTagsByNewsID);

        return listOfNewsTagsByNewsID.size();
    }

    @Override
    public int deleteNewsTagByTagID(int tagID) {

        List<NewsTag> listOfNewsTagsByTagID = newsTagRepository.findNewsTagsByTagID(tagID);

        isNewsTagListEmpty(listOfNewsTagsByTagID);

        newsTagRepository.deleteAll(listOfNewsTagsByTagID);

        return listOfNewsTagsByTagID.size();
    }

    @Override
    public void deleteNewsTag(int newsID, int tagID) {

        Optional<NewsTag> newsTagOptional = newsTagRepository.findNewsTagsByNewsIDAndTagID(newsID, tagID);

        NewsTag newsTag = newsTagOptional.orElseThrow(() ->
                new ResourceNotFoundException("NewsTag not Found", "NewsTag"));

        newsTagRepository.delete(newsTag);
    }

    @Override
    public NewsTagDto getNewsTagByID(int newsTagID) {

        Optional<NewsTag> newsTagOptional = newsTagRepository.findById(newsTagID);

        NewsTag newsTag = newsTagOptional.orElseThrow(() ->
                new ResourceNotFoundException("NewsTag not Found", "NewsTag"));

        return newsTagMapper.mapNewsTagToNewsTagDto(newsTag);
    }

    @Override
    public List<NewsTagDetailedDto> getTagsOfNewsDetailed(int newsID) {

        List<NewsTag> listOfNewsTagByNewsID = newsTagRepository.findNewsTagsByNewsID(newsID);

        isNewsTagListEmpty(listOfNewsTagByNewsID);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTagByNewsID);
    }

    @Override
    public List<NewsTagDto> getTagsOfNews(int newsID) {

        List<NewsTag> listOfNewsTagByNewsID = newsTagRepository.findNewsTagsByNewsID(newsID);

        isNewsTagListEmpty(listOfNewsTagByNewsID);

        return newsTagMapper.mapListToNewsTagDto(listOfNewsTagByNewsID);
    }

    @Override
    public List<NewsTagDetailedDto> getNewsOfTag(int tagID) {

        List<NewsTag> listOfNewsTagByTagID = newsTagRepository.findNewsTagsByTagID(tagID);

        isNewsTagListEmpty(listOfNewsTagByTagID);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTagByTagID);
    }

    @Override
    public List<NewsTagDetailedDto> getAllNewsTags() {

        List<NewsTag> listOfNewsTag = newsTagRepository.findAll();

        isNewsTagListEmpty(listOfNewsTag);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTag);
    }

    private void isNewsTagListEmpty(List<NewsTag> newsTagList) {

        if (newsTagValidation.isListEmpty(newsTagList)) {

            throw new ResourceNotFoundException("Tags for News not Found", "NewsTag");
        }
    }

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
