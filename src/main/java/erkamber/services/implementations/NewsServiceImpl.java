package erkamber.services.implementations;

import erkamber.configurations.JsonObjectConfiguration;
import erkamber.dtos.*;
import erkamber.entities.*;
import erkamber.enums.TopTrendingNewsListSize;
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
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final JsonObjectConfiguration jsonObjectConfiguration;

    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper newsMapper, NewsValidation newsValidation,
                           UserService userService, NewsTagRepository newsTagRepository, MediaService mediaService,
                           CommentService commentService, VoteRepository voteRepository, VoteMapper voteMapper,
                           TagService tagService, UserValidation userValidation, UserRepository userRepository,
                           ViewService viewService, ViewRepository viewRepository, InjectionValidation injectionValidation,
                           JsonObjectConfiguration jsonObjectConfiguration) {

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
        this.jsonObjectConfiguration = jsonObjectConfiguration;
    }

    /**
     * Retrieves basic information about a news article by its ID.
     *
     * @param newsID The ID of the news article to retrieve information for.
     * @return A DTO containing basic information about the requested news article.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found.
     */
    @Override
    public NewsDto getNewsByNewsID(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        return newsMapper.mapNewsToNewsDto(searchedNews);
    }

    /**
     * Retrieves detailed information about a news article by its ID.
     *
     * @param newsID The ID of the news article to retrieve detailed information for.
     * @return A detailed DTO containing information about the requested news article.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found.
     */
    @Override
    public NewsDetailedDto getNewsDetailedByNewsID(int newsID) {

        return gatherDataForNewsDetailedDtoByNewsID(newsID);
    }

    /**
     * Retrieves detailed information about a news article for a logged-in user.
     *
     * @param newsID The ID of the news article to retrieve.
     * @param userID The ID of the logged-in user.
     * @return A detailed DTO containing information about the requested news article.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found, or if the user does not exist.
     */
    @Override
    public NewsDetailedDto getNewsAsLoggedUser(int userID, int newsID) {

        isUserExists(userID);

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        View view = new View(LocalDate.now(), newsID, userID);

        viewRepository.save(view);

        return gatherDataForNewsDetailedDtoByNewsID(searchedNews.getNewsID());
    }

    /**
     * Adds a new news article.
     *
     * @param newsDto The data transfer object containing information about the news article.
     * @return The ID of the newly added news article.
     * @throws InvalidInputException     If the provided news title or content is invalid.
     * @throws ResourceNotFoundException If the user with the specified ID does not exist.
     * @throws TextInjectionException    If the provided content contains potential SQL or general code injection.
     */
    @Override
    public int addNews(NewsDto newsDto) {

        LocalDateTime localDateTime = LocalDateTime.now();

        newsDto.setNewsCreationDate(localDateTime);

        isUserExists(newsDto.getUserID());

        validateNewsTitle(newsDto.getNewsTitle());

        //validateTextForInjection(newsDto.getNewsContent());

        News news = newsMapper.mapNewsDtoToNews(newsDto);

        newsRepository.save(news);

        return news.getNewsID();
    }

    /**
     * Updates the content of a news article.
     *
     * @param content The new content to be set for the news article.
     * @param newsID  The ID of the news article to be updated.
     * @param userID  The ID of the user requesting the update.
     * @throws InvalidInputException     If the provided content is invalid.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found, or if the user does not exist.
     * @throws InvalidInputException     If the requesting user is not the author of the news article.
     * @throws TextInjectionException    If the provided content contains potential SQL or general code injection.
     */
    @Override
    public void updateNewsContent(String content, int newsID, int userID) {

        isUserExists(userID);

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        isUserTheAuthorOfNews(searchedNews, userID);

        JSONObject jsonObject = jsonObjectConfiguration.getJsonObjectConfiguration(content);

        //validateTextForInjection(jsonObject.getString("content"));

        searchedNews.setNewsContent(jsonObject.getString("content"));

        newsRepository.save(searchedNews);

    }

    /**
     * Updates the title of a news article.
     *
     * @param title  The new title for the news article.
     * @param newsID The ID of the news article to be updated.
     * @param userID The ID of the user requesting the update.
     * @throws ResourceNotFoundException If the news article or user is not found.
     * @throws InvalidInputException     If the provided title is invalid.
     */
    @Override
    public void updateNewsTitle(String title, int newsID, int userID) {

        isUserExists(userID);

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        isUserTheAuthorOfNews(searchedNews, userID);

        JSONObject jsonObject = jsonObjectConfiguration.getJsonObjectConfiguration(title);

        validateNewsTitle(jsonObject.getString("title"));

        searchedNews.setNewsTitle(jsonObject.getString("title"));

        newsRepository.save(searchedNews);
    }

    /**
     * Deletes a news article by its ID.
     *
     * @param newsID The ID of the news article to be deleted.
     * @throws ResourceNotFoundException If the news article is not found.
     */
    @Override
    public void deleteNewsByID(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        newsRepository.delete(searchedNews);
    }

    /**
     * Deletes all news articles authored by a specific user.
     *
     * @param authorID The ID of the author whose news articles will be deleted.
     * @throws ResourceNotFoundException If the user or their news articles are not found.
     */
    @Override
    public void deleteNewsByAuthorID(int authorID) {

        isUserExists(authorID);

        List<News> newsListOfAuthor = newsRepository.findNewsByUserIDOrderByNewsIDAsc(authorID);

        isListOfNewsEmpty(newsListOfAuthor);

        newsRepository.deleteAll(newsListOfAuthor);
    }

    /**
     * Retrieves a list of detailed NewsDtos representing all news articles.
     *
     * @return A list of NewsDetailedDtos representing all news articles.
     * @throws ResourceNotFoundException If there are no news articles.
     */
    @Override
    public List<NewsDetailedDto> getAllNews() {

        List<News> listOfAllNews = newsRepository.findAll((Sort.by(Sort.Direction.ASC, "newsID")));

        isListOfNewsEmpty(listOfAllNews);

        return convertListToNewsDetailedDto(listOfAllNews);
    }

    /**
     * Retrieves a list of detailed NewsDtos representing news articles authored by a specific user.
     *
     * @param userID The ID of the user whose authored news articles will be retrieved.
     * @return A list of NewsDetailedDtos representing news articles authored by the specified user.
     * @throws ResourceNotFoundException If the user or their news articles are not found.
     */
    @Override
    public List<NewsDetailedDto> findNewsByUserID(int userID) {

        isUserExists(userID);

        List<News> listOfNewsByUser = newsRepository.findNewsByUserIDOrderByNewsIDAsc(userID);

        isListOfNewsEmpty(listOfNewsByUser);

        return convertListToNewsDetailedDto(listOfNewsByUser);
    }

    /**
     * Retrieves a list of detailed NewsDtos representing news articles with a specific title.
     *
     * @param newsTitle The title of the news articles to be retrieved.
     * @return A list of NewsDetailedDtos representing news articles with the specified title.
     * @throws ResourceNotFoundException If news articles with the specified title are not found.
     * @throws InvalidInputException     If the provided news title is invalid.
     */
    @Override
    public List<NewsDetailedDto> findNewsByNewsTitle(String newsTitle) {

        validateNewsTitle(newsTitle);

        //validateTextForInjection(newsTitle);

        List<News> listOfNewsByTitle = newsRepository.findNewsByNewsTitleOrderByNewsIDAsc(newsTitle);

        isListOfNewsEmpty(listOfNewsByTitle);

        return convertListToNewsDetailedDto(listOfNewsByTitle);
    }

    /**
     * Retrieves a list of detailed NewsDtos representing news articles created before a specified date.
     *
     * @param beforeDate The date before which the news articles were created.
     * @return A list of NewsDetailedDtos representing news articles created before the specified date.
     * @throws ResourceNotFoundException If news articles created before the specified date are not found.
     */
    @Override
    public List<NewsDetailedDto> findNewsByCreationDateBefore(LocalDate beforeDate) {

        List<News> listOfNewsByCreationDateBefore = newsRepository.findNewsByNewsCreationDateBeforeOrderByNewsIDAsc(beforeDate);

        isListOfNewsEmpty(listOfNewsByCreationDateBefore);

        return convertListToNewsDetailedDto(listOfNewsByCreationDateBefore);
    }

    /**
     * Retrieves a list of detailed NewsDtos representing news articles created after a specified date.
     *
     * @param afterDate The date after which the news articles were created.
     * @return A list of NewsDetailedDtos representing news articles created after the specified date.
     * @throws ResourceNotFoundException If news articles created after the specified date are not found.
     */
    @Override
    public List<NewsDetailedDto> findNewsByCreationDateAfter(LocalDate afterDate) {

        List<News> listOfNewsByCreationDateAfter = newsRepository.findNewsByNewsCreationDateAfterOrderByNewsIDAsc(afterDate);

        isListOfNewsEmpty(listOfNewsByCreationDateAfter);

        return convertListToNewsDetailedDto(listOfNewsByCreationDateAfter);
    }

    /**
     * Retrieves the top trending news articles based on view counts within the last three days.
     *
     * @param countOfTrendingNews The number of top trending news articles to retrieve.
     * @return A list of {@link NewsDetailedDto} representing the top trending news articles.
     */
    @Override
    public List<NewsDetailedDto> getTopTrendingNews(int countOfTrendingNews) {

        TopTrendingNewsListSize.TRENDING_NEWS_LIST_SIZE.setTopTrendingNewsListSize(countOfTrendingNews);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(3);

        List<View> listOfViews = getViewsWithinDateRange(endDate, startDate);
        List<Integer> mostCommonViewNewsIDs = getMostCommonViewNewsIDs(listOfViews);

        List<News> listOfTopTrendingNews = fetchTopTrendingNews(mostCommonViewNewsIDs);

        return convertListToNewsDetailedDto(listOfTopTrendingNews);
    }

    /**
     * Converts a list of News objects to a list of detailed NewsDetailedDto objects.
     *
     * @param listOfNews The list of News objects to be converted.
     * @return A list of NewsDetailedDto objects representing the detailed information of news articles.
     */
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

    /**
     * Validates whether a list of news articles is empty or not.
     *
     * @param listOfNews The list of news articles to be validated.
     * @throws ResourceNotFoundException If the list of news articles is empty.
     */
    private void isListOfNewsEmpty(List<News> listOfNews) {

        if (newsValidation.isListEmpty(listOfNews)) {

            throw new ResourceNotFoundException("No News Found", "News");
        }
    }

    /**
     * Validates whether a user is the author of a specific news article.
     *
     * @param news   The news article for which the authorship will be validated.
     * @param userID The ID of the user to be validated as the author.
     * @throws InvalidInputException If the provided user is not the author of the news article.
     */
    private void isUserTheAuthorOfNews(News news, int userID) {

        if (!newsValidation.isUserTheAuthorOfNews(news.getUserID(), userID)) {

            throw new InvalidInputException("Provided User with ID" + userID + " is not the Author");
        }
    }

    /**
     * Gathers data to create a detailed NewsDetailedDto for a specific news article by its ID.
     *
     * @param newsID The ID of the news article for which detailed data will be gathered.
     * @return A NewsDetailedDto containing detailed information about the specified news article.
     * @throws ResourceNotFoundException If the news article or related data is not found.
     */
    private NewsDetailedDto gatherDataForNewsDetailedDtoByNewsID(int newsID) {

        // Retrieve the news article by its ID
        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);
        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        // Retrieve the user data of the news article's author
        UserDto userDto = userService.getUserByID(searchedNews.getUserID());

        // Retrieve the tags associated with the news article
        List<TagDto> listOfTags = getTagsOfNews(newsID);

        // Retrieve the media files associated with the news article
        List<MediaDto> listOfMediasForNews = mediaService.findMediaByNewsID(newsID);

        // Retrieve the comments associated with the news article
        List<CommentDetailedDto> listOfCommentsForNews = commentService.getCommentsByNewsID(newsID);

        // Retrieve the upvotes and downvotes for the news article
        List<Vote> listOfUpVotesForNews = voteRepository.getVoteByIsUpVoteAndVotedContentIDAndVotedContentType(true, newsID, VoteTypeNews.NEWS.getType());
        List<VoteDto> listOfUpVotesDto = voteMapper.mapListOfVoteToMVoteDto(listOfUpVotesForNews);

        List<Vote> listOfDownVotesForNews = voteRepository.getVoteByIsUpVoteAndVotedContentIDAndVotedContentType(false, newsID, VoteTypeNews.NEWS.getType());
        List<VoteDto> listOfDownVotesDto = voteMapper.mapListOfVoteToMVoteDto(listOfDownVotesForNews);

        // Retrieve the number of views for the news article
        int numberOfViews = viewService.getNumberOfViewsOfNews(newsID);

        // Map the gathered data to create a detailed NewsDetailedDto
        return newsMapper.mapToNewsDtoDetailed(newsMapper.mapNewsToNewsDto(searchedNews), userDto, numberOfViews, listOfTags,
                listOfMediasForNews, listOfCommentsForNews, listOfUpVotesDto, listOfDownVotesDto);
    }

    /**
     * Retrieves a list of TagDto objects representing the tags associated with a specific news article.
     *
     * @param newsID The ID of the news article for which tags will be retrieved.
     * @return A list of TagDto objects representing the tags associated with the specified news article.
     */
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

    /**
     * Validates whether a user with the specified user ID exists.
     *
     * @param userID The ID of the user to be validated for existence.
     * @throws InvalidInputException If the user with the specified user ID does not exist.
     */
    private void isUserExists(int userID) {

        List<User> allUsers = userRepository.findAll();

        if (!userValidation.isUserExistsByUserID(allUsers, userID)) {

            throw new InvalidInputException("Not existing User");
        }
    }

    /**
     * Validates whether the provided text contains potential SQL or general code injection.
     *
     * @param text The text to be validated for injection.
     * @throws TextInjectionException If the text contains potential SQL or code injection.
     */
    private void validateTextForInjection(String text) {

        if (injectionValidation.isTextContainingSqlInjection(text)) {

            throw new TextInjectionException("Invalid News Content. Text might contain SQL Injection!!");
        }
        if (injectionValidation.isTextContainingInjection(text)) {

            throw new TextInjectionException("Invalid News Content. Text might contain Injection!!");
        }
    }

    /**
     * Updates the vote count of a news article by swapping votes.
     *
     * @param newsID   The ID of the news article for which votes will be swapped.
     * @param isUpVote A boolean indicating whether the vote to be swapped is an upvote or not.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found.
     */
    @Transactional
    protected void updateNewsVoteBySwappingVotes(int newsID, boolean isUpVote) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        int numberOfUpVotes = searchedNews.getNewsUpVotes();

        int numberOfDownVotes = searchedNews.getNewsDownVotes();

        // Update the vote counts based on the vote being swapped
        if (isUpVote) {

            searchedNews.setNewsUpVotes(numberOfUpVotes + 1);

            searchedNews.setNewsDownVotes(numberOfDownVotes - 1);

        } else {

            searchedNews.setNewsUpVotes(numberOfUpVotes - 1);

            searchedNews.setNewsDownVotes(numberOfDownVotes + 1);
        }
        newsRepository.save(searchedNews);
    }

    /**
     * Adds a new upvote to a news article.
     *
     * @param newsID The ID of the news article to which a new upvote will be added.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found.
     */
    protected void addNewsNewUpVote(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        int numberOfUpVotes = searchedNews.getNewsUpVotes();

        searchedNews.setNewsUpVotes(numberOfUpVotes + 1);

        newsRepository.save(searchedNews);
    }

    /**
     * Adds a new downvote to a news article.
     *
     * @param newsID The ID of the news article to which a new downvote will be added.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found.
     */
    protected void addNewsNewDownVote(int newsID) {

        Optional<News> searchedNewsOptional = newsRepository.findById(newsID);

        News searchedNews = searchedNewsOptional.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + newsID, "News"));

        int numberOfDownVotes = searchedNews.getNewsDownVotes();

        searchedNews.setNewsDownVotes(numberOfDownVotes + 1);

        newsRepository.save(searchedNews);
    }

    /**
     * Updates the vote count of a news article by removing a vote.
     *
     * @param newsID   The ID of the news article for which a vote will be removed.
     * @param isUpVote A boolean indicating whether the vote to be removed is an upvote or not.
     * @throws ResourceNotFoundException If the news article with the specified ID is not found.
     */
    @Transactional
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

    /**
     * Validates whether the provided news title is valid.
     *
     * @param title The news title to be validated.
     * @throws InvalidInputException If the provided news title is invalid.
     */
    private void validateNewsTitle(String title) {

        if (!newsValidation.isNewsTitleValid(title)) {

            throw new InvalidInputException("Invalid News Title!");
        }
    }

    /**
     * Retrieves a list of View objects that fall within the specified date range.
     *
     * @param endDate   The end date of the date range.
     * @param startDate The start date of the date range.
     * @return A list of View objects representing views created within the specified date range.
     */
    private List<View> getViewsWithinDateRange(LocalDate endDate, LocalDate startDate) {

        return viewRepository.findViewByViewCreationDateBetween(endDate, startDate);
    }

    /**
     * Retrieves a list of the most common viewNewsIDs based on the count of views.
     *
     * @param listOfViews The list of View objects containing viewNewsIDs and their associated view counts.
     * @return A list of Integer values representing the most common viewNewsIDs.
     */
    private List<Integer> getMostCommonViewNewsIDs(List<View> listOfViews) {

        Map<Integer, Long> countByViewNewsID = listOfViews.stream()
                .collect(Collectors.groupingBy(View::getViewNewsID, Collectors.counting()));

        return countByViewNewsID.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(TopTrendingNewsListSize.TRENDING_NEWS_LIST_SIZE.getTopTrendingNewsListSize())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Fetches the top trending news articles based on a list of common viewNewsIDs.
     *
     * @param mostCommonViewNewsIDs The list of common viewNewsIDs representing the top trending articles.
     * @return A list of {@link News} objects representing the top trending news articles.
     */
    private List<News> fetchTopTrendingNews(List<Integer> mostCommonViewNewsIDs) {

        Stream<News> trendingNewsStream = mostCommonViewNewsIDs.stream()
                .map(newsRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get);

        return trendingNewsStream.collect(Collectors.toList());
    }
}
