package erkamber.services.implementations;

import erkamber.dtos.NewsDto;
import erkamber.entities.News;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.NewsMapper;
import erkamber.repositories.NewsRepository;
import erkamber.services.interfaces.NewsService;
import erkamber.validations.NewsValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    private final NewsValidation newsValidation;

    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper newsMapper, NewsValidation newsValidation) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.newsValidation = newsValidation;
    }

    @Override
    public NewsDto getNewsByNewsID(int newsID) {

        return null;
    }

    @Override
    public NewsDto getNews(int newsID, int userID) {

        return null;
    }

    @Override
    public int addNews(NewsDto newsDto) {
        return 0;
    }

    @Override
    public int updateNews(NewsDto newsDto, int userID) {
        return 0;
    }

    @Override
    public int updateNewsContent(String content, int newsID, int userID) {
        return 0;
    }

    @Override
    public int updateNewsTitle(String title, int newsID, int userID) {
        return 0;
    }

    @Override
    public NewsDto findNewsByNewsID(int newsID) {
        return null;
    }

    @Override
    public List<NewsDto> getAllNews() {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByUserID(int userID) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByUserName(String userName) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByNewsTitle(String newsTitle) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByUpVotesLowerThan(int upVotesCount) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByUpVotesGreaterThan(int upVotesCount) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByDownVotesLowerThan(int downVotesCount) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByDownVotesGreaterThan(int downVotesCount) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByCreationDateBefore(LocalDate beforeDate) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByCreationDateAfter(LocalDate afterDate) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByCreationBetween(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<NewsDto> findNewsByContentContaining(String content) {
        return null;
    }

    protected News getNewsOfComment(int newsID) {

        Optional<News> newsOfComment = newsRepository.findById(newsID);

        return newsOfComment.orElseThrow(() ->
                new ResourceNotFoundException("News not Found:" + newsID, "News"));
    }
}
