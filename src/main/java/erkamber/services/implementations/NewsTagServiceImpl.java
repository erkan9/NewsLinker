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

    public NewsTagServiceImpl(NewsTagRepository newsTagRepository, NewsTagMapper newsTagMapper, NewsService newsService, TagService tagService) {
        this.newsTagRepository = newsTagRepository;
        this.newsTagMapper = newsTagMapper;
        this.newsService = newsService;
        this.tagService = tagService;
    }

    @Override
    public int addNewTag(NewsTagDto newsTagDto) {

        NewsTag newsTag = newsTagMapper.mapNewsTagToNewsTagDto(newsTagDto);

        newsTagRepository.save(newsTag);

        return newsTag.getNewsTagID();
    }

    @Override
    public int deleteAllTagsByNewsID(int newsID) {

        List<NewsTag> listOfNewsTags = newsTagRepository.findNewsTagsByNewsID(newsID);

        newsTagRepository.deleteAll(listOfNewsTags);

        return listOfNewsTags.size();
    }

    @Override
    public int deleteNewsTagByTagID(int tagID) {

        List<NewsTag> listOfNewsTags = newsTagRepository.findNewsTagsByTagID(tagID);

        newsTagRepository.deleteAll(listOfNewsTags);

        return listOfNewsTags.size();
    }

    @Override
    public void deleteNewsTag(int newsID, int tagID) {

        Optional<NewsTag> newsTagOptional = newsTagRepository.findNewsTagsByNewsIDAndTagID(newsID, tagID);

        NewsTag newsTag = newsTagOptional.orElseThrow(() ->
                new ResourceNotFoundException("NewsTag not Found", "NewsTag"));

        newsTagRepository.delete(newsTag);
    }

    @Override
    public List<NewsTagDetailedDto> getTagsOfNewsDetailed(int newsID) {

        List<NewsTag> listOfNewsTag = newsTagRepository.findNewsTagsByNewsID(newsID);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTag);
    }

    @Override
    public List<NewsTagDto> getTagsOfNews(int newsID) {

        List<NewsTag> listOfNewsTag = newsTagRepository.findNewsTagsByNewsID(newsID);

        return newsTagMapper.mapListToNewsTagDto(listOfNewsTag);
    }

    @Override
    public List<NewsTagDetailedDto> getNewsOfTag(int tagID) {

        List<NewsTag> listOfNewsTag = newsTagRepository.findNewsTagsByTagID(tagID);

        return convertListToListOfNewsTagDetailedDto(listOfNewsTag);
    }

    @Override
    public List<NewsTagDetailedDto> getAllNewsTags() {

        List<NewsTag> listOfNewsTag = newsTagRepository.findAll();

        return convertListToListOfNewsTagDetailedDto(listOfNewsTag);
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
