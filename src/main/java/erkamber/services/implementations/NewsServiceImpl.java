package erkamber.services.implementations;

import erkamber.dtos.*;
import erkamber.entities.*;
import erkamber.enums.VoteTypeNews;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.exceptions.TextInjectionException;
import erkamber.mappers.NewsMapper;
import erkamber.mappers.VoteMapper;
import erkamber.repositories.*;
import erkamber.services.interfaces.*;
import erkamber.validations.InjectionValidation;
import erkamber.validations.NewsValidation;
import erkamber.validations.UserValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    private final NewsValidation newsValidation;

    private final UserService userService;

    private final NewsTagRepository newsTagRepository;

    private final MediaService mediaService;

    private final CommentService commentService;

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final TagService tagService;

    private final UserValidation userValidation;

    private final UserRepository userRepository;

    private final ViewService viewService;

    private final ViewRepository viewRepository;

    private final InjectionValidation injectionValidation;

    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper newsMapper, NewsValidation newsValidation,
                           UserService userService, NewsTagRepository newsTagRepository, MediaService mediaService,
                           CommentService commentService, VoteRepository voteRepository, VoteMapper voteMapper, TagService tagService, UserValidation userValidation,
                           UserRepository userRepository, ViewService viewService, ViewRepository viewRepository, InjectionValidation injectionValidation) {

        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.newsValidation = newsValidation;
        this.userService = userService;
        this.newsTagRepository = newsTagRepository;
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
        this.tagService = tagService;
        this.userValidation = userValidation;
        this.userRepository = userRepository;
        this.viewService = viewService;
        this.viewRepository = viewRepository;
        this.injectionValidation = injectionValidation;
    }

    @Override
    public NewsDto getNewsByNewsID(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        return newsMapper.mapNewsToNewsDto(searchedNews);
    }

    @Override
    public NewsDetailedDto getNewsDetailedByNewsID(int newsID) {

        return gatherDataForNewsDetailedDtoByNewsID(newsID);
    }

    @Override
    public NewsDetailedDto getNewsAsLoggedUser(int newsID, int userID) {

        isUserExists(userID);

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        View view = new View(LocalDate.now(), newsID, userID);

        viewRepository.save(view);

        return gatherDataForNewsDetailedDtoByNewsID(newsID);
    }

    @Override
    public int addNews(NewsDto newsDto) {

        isUserExists(newsDto.getUserID());

        validateNewsTitle(newsDto.getNewsTitle());

        validateTextForInjection(newsDto.getNewsContent());

        News news = newsMapper.mapNewsDtoToNews(newsDto);

        newsRepository.save(news);

        return news.getNewsID();
    }

    @Override
    public void updateNewsContent(String content, int newsID, int userID) {

        isUserExists(userID);

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        isUserTheAuthorOfNews(searchedNews, userID);

        validateTextForInjection(content);

        searchedNews.setNewsContent(content);

        newsRepository.save(searchedNews);

    }

    @Override
    public void updateNewsTitle(String title, int newsID, int userID) {

        isUserExists(userID);

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        isUserTheAuthorOfNews(searchedNews, userID);

        validateNewsTitle(title);

        searchedNews.setNewsTitle(title);

        newsRepository.save(searchedNews);
    }

    @Override
    public List<NewsDetailedDto> getAllNews() {

        List<News> listOfAllNews = newsRepository.findAll();

        return convertListToNewsDetailedDto(listOfAllNews);
    }

    @Override
    public List<NewsDetailedDto> findNewsByUserID(int userID) {

        isUserExists(userID);

        List<News> listOfNewsByUser = newsRepository.findNewsByUserID(userID);

        return convertListToNewsDetailedDto(listOfNewsByUser);
    }

    @Override
    public List<NewsDetailedDto> findNewsByNewsTitle(String newsTitle) {

        validateNewsTitle(newsTitle);

        validateTextForInjection(newsTitle);

        List<News> listOfNewsByTitle = newsRepository.findNewsByNewsTitle(newsTitle);

        return convertListToNewsDetailedDto(listOfNewsByTitle);
    }

    @Override
    public List<NewsDetailedDto> findNewsByCreationDateBefore(LocalDate beforeDate) {

        List<News> listOfNewsByCreationDateBefore = newsRepository.findNewsByNewsCreationDateBefore(beforeDate);

        return convertListToNewsDetailedDto(listOfNewsByCreationDateBefore);
    }

    @Override
    public List<NewsDetailedDto> findNewsByCreationDateAfter(LocalDate afterDate) {

        List<News> listOfNewsByCreationDateAfter = newsRepository.findNewsByNewsCreationDateAfter(afterDate);

        return convertListToNewsDetailedDto(listOfNewsByCreationDateAfter);
    }

    private List<NewsDetailedDto> convertListToNewsDetailedDto(List<News> listOfNews) {

        List<NewsDetailedDto> listOfNewsDetailedDtoList = new ArrayList<>();

        for (News news : listOfNews) {

            NewsDetailedDto newsDetailedDto = gatherDataForNewsDetailedDtoByNewsID(news.getNewsID());

            listOfNewsDetailedDtoList.add(newsDetailedDto);
        }

        return listOfNewsDetailedDtoList;
    }

    protected News getNewsOfComment(int newsID) {

        Optional<News> newsOfComment = newsRepository.findById(newsID);

        return newsOfComment.orElseThrow(() ->
                new ResourceNotFoundException("News not Found:" + newsID, "News"));
    }

    private void isUserTheAuthorOfNews(News news, int userID) {

        if (newsValidation.isUserTheAuthorOfNews(news.getUserID(), userID)) {

            throw new InvalidInputException("Provided User with ID" + userID + "is not the Author");
        }
    }

    private NewsDetailedDto gatherDataForNewsDetailedDtoByNewsID(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        UserDto userDto = userService.getUserByID(searchedNews.getUserID());

        List<TagDto> listOfTags = getTagsOfNews(newsID);

        List<MediaDto> listOfMediasForNews = mediaService.findMediaByNewsID(newsID);

        List<CommentDetailedDto> listOfCommentsForNews = commentService.getCommentsByNewsID(newsID);

        List<Vote> listOfUpVotesForNews = voteRepository.getVoteByIsUpVoteAndVotedContentIDAndVotedContentType(true, newsID, VoteTypeNews.NEWS.getType());

        List<VoteDto> listOfUpVotesDto = voteMapper.mapListOfVoteToMVoteDto(listOfUpVotesForNews);

        List<Vote> listOfDownVotesForNews = voteRepository.getVoteByIsUpVoteAndVotedContentIDAndVotedContentType(false, newsID, VoteTypeNews.NEWS.getType());

        List<VoteDto> listOfDownVotesDto = voteMapper.mapListOfVoteToMVoteDto(listOfDownVotesForNews);

        int numberOfViews = viewService.getNumberOfViewsOfNews(newsID);

        return newsMapper.mapToNewsDtoDetailed(newsMapper.mapNewsToNewsDto(searchedNews), userDto, numberOfViews, listOfTags,
                listOfMediasForNews, listOfCommentsForNews, listOfUpVotesDto, listOfDownVotesDto);
    }

    private List<TagDto> getTagsOfNews(int newsID) {

        List<NewsTag> listOfTagsForNews = newsTagRepository.findNewsTagsByNewsID(newsID);

        List<TagDto> listOfTags = new ArrayList<>();

        for (NewsTag newsTag : listOfTagsForNews) {

            int tagID = newsTag.getTagID();

            TagDto tagDto = tagService.findTagByTagID(tagID);

            listOfTags.add(tagDto);
        }

        return listOfTags;
    }

    private void isUserExists(int userID) {

        List<User> allUsers = userRepository.findAll();

        if (!userValidation.isUserExistsByUserID(allUsers, userID)) {

            throw new InvalidInputException("Not existing User");
        }
    }

    private void validateTextForInjection(String text) {

        if (injectionValidation.isTextContainingSqlInjection(text)) {

            throw new TextInjectionException("Invalid News Content. Text might contain SQL Injection!!");
        }
        if (injectionValidation.isTextContainingInjection(text)) {

            throw new TextInjectionException("Invalid News Content. Text might contain Injection!!");
        }
    }

    protected void updateNewsVoteBySwappingVotes(int newsID, boolean isUpVote) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        int numberOfUpVotes = searchedNews.getNewsUpVotes();

        int numberOfDownVotes = searchedNews.getNewsDownVotes();

        if (isUpVote) {

            searchedNews.setNewsUpVotes(numberOfUpVotes + 1);

            searchedNews.setNewsDownVotes(numberOfDownVotes - 1);

        } else {

            searchedNews.setNewsUpVotes(numberOfUpVotes - 1);

            searchedNews.setNewsDownVotes(numberOfDownVotes + 1);
        }
        newsRepository.save(searchedNews);
    }

    protected void addNewsNewUpVote(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        int numberOfUpVotes = searchedNews.getNewsUpVotes();

        searchedNews.setNewsUpVotes(numberOfUpVotes + 1);

        newsRepository.save(searchedNews);
    }

    protected void addNewsNewDownVote(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        int numberOfDownVotes = searchedNews.getNewsDownVotes();

        searchedNews.setNewsDownVotes(numberOfDownVotes + 1);

        newsRepository.save(searchedNews);
    }

    protected void updateNewsVoteByRemovingVote(int newsID, boolean isUpVote) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        if (isUpVote) {

            int numberOfUpVotes = searchedNews.getNewsUpVotes();

            searchedNews.setNewsUpVotes(numberOfUpVotes - 1);

        } else {

            int numberOfDownVotes = searchedNews.getNewsDownVotes();

            searchedNews.setNewsDownVotes(numberOfDownVotes - 1);
        }

        newsRepository.save(searchedNews);
    }

    private void validateNewsTitle(String title) {

        if (!newsValidation.isNewsTitleValid(title)) {

            throw new InvalidInputException("Invalid News Title!");
        }
    }
}
