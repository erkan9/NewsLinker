package erkamber.services.implementations;

import erkamber.dtos.UserDto;
import erkamber.dtos.ViewDetailedDto;
import erkamber.dtos.ViewDto;
import erkamber.entities.News;
import erkamber.entities.View;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.NewsMapper;
import erkamber.mappers.ViewMapper;
import erkamber.repositories.NewsRepository;
import erkamber.repositories.ViewRepository;
import erkamber.services.interfaces.UserService;
import erkamber.services.interfaces.ViewService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ViewServiceImpl implements ViewService {

    private final ViewMapper viewMapper;

    private final ViewRepository viewRepository;

    private final UserService userService;

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    public ViewServiceImpl(ViewMapper viewMapper, ViewRepository viewRepository, UserService userService,
                           NewsRepository newsRepository, NewsMapper newsMapper) {

        this.viewMapper = viewMapper;
        this.viewRepository = viewRepository;
        this.userService = userService;
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    /**
     * Deletes a View based on its ID.
     *
     * @param viewID The ID of the View to be deleted.
     * @throws ResourceNotFoundException If the View with the specified ID is not found.
     */
    @Override
    public void deleteViewsByViewID(int viewID) {

        View view = viewRepository.findById(viewID)
                .orElseThrow(() -> new ResourceNotFoundException("View with that ID, could not be found", "View"));

        viewRepository.deleteById(viewID);
    }

    /**
     * Deletes all Views associated with a specific user ID.
     *
     * @param userID The ID of the user whose Views will be deleted.
     */
    @Override
    public void deleteViewsByUserID(int userID) {

        List<View> viewsByUserID = viewRepository.findViewByViewUserID(userID);

        viewRepository.deleteAll(viewsByUserID);
    }

    /**
     * Deletes all Views associated with a specific news article ID.
     *
     * @param newsID The ID of the news article whose associated Views will be deleted.
     */
    @Override
    public void deleteViewsByNewsID(int newsID) {

        List<View> viewsByNewsID = viewRepository.findViewByViewNewsID(newsID);

        viewRepository.deleteAll(viewsByNewsID);
    }

    /**
     * Adds a new View based on the information provided in the ViewDto.
     *
     * @param viewDto The ViewDto containing the information for the new View.
     * @return The ID of the newly added View.
     */
    @Override
    public int addNewView(ViewDto viewDto) {

        viewDto.setViewCreationDate(LocalDate.now());

        View newView = viewMapper.mapViewDtoToView(viewDto);

        viewRepository.save(newView);

        return newView.getViewID();
    }

    /**
     * Retrieves the number of Views associated with a specific news article ID.
     *
     * @param newsID The ID of the news article for which the number of Views will be retrieved.
     * @return The number of Views associated with the specified news article.
     */
    @Override
    public int getNumberOfViewsOfNews(int newsID) {

        List<View> viewsByNewsID = viewRepository.findViewByViewNewsID(newsID);

        return viewsByNewsID.size();
    }

    /**
     * Retrieves a detailed representation of a View based on its ID.
     *
     * @param viewID The ID of the View to be retrieved.
     * @return A detailed DTO (Data Transfer Object) representation of the requested View.
     * @throws ResourceNotFoundException If the View with the specified ID is not found.
     */
    @Override
    public ViewDetailedDto getViewByID(int viewID) {

        View view = viewRepository.findById(viewID)
                .orElseThrow(() -> new ResourceNotFoundException("View with that ID, could not be found", "View"));

        return fetchViewDetailedDtoData(view);
    }

    /**
     * Retrieves a list of detailed DTOs (Data Transfer Objects) representing all Views.
     *
     * @return A list of detailed DTOs representing all Views.
     */
    @Override
    public List<ViewDetailedDto> getAllViews() {

        List<View> listOfAllViews = viewRepository.findAll();

        return convertListOfViewToViewDetailedDto(listOfAllViews);
    }

    /**
     * Retrieves a list of detailed DTOs (Data Transfer Objects) representing Views associated with a specific news article ID.
     *
     * @param newsID The ID of the news article for which associated Views will be retrieved.
     * @return A list of detailed DTOs representing Views associated with the specified news article.
     */
    @Override
    public List<ViewDetailedDto> findViewByViewNewsID(int newsID) {

        List<View> viewsByNewsID = viewRepository.findViewByViewNewsID(newsID);

        return convertListOfViewToViewDetailedDto(viewsByNewsID);
    }

    /**
     * Retrieves a list of detailed DTOs (Data Transfer Objects) representing Views associated with a specific news article ID and creation date.
     *
     * @param newsID       The ID of the news article for which associated Views will be retrieved.
     * @param creationDate The creation date for which associated Views will be retrieved.
     * @return A list of detailed DTOs representing Views associated with the specified news article and creation date.
     */
    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDate(int newsID, LocalDate creationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDate(newsID, creationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    /**
     * Retrieves a list of detailed DTOs (Data Transfer Objects) representing Views associated with a specific news article ID and creation dates before a certain date.
     *
     * @param newsID             The ID of the news article for which associated Views will be retrieved.
     * @param beforeCreationDate The upper bound of the creation date range for which associated Views will be retrieved.
     * @return A list of detailed DTOs representing Views associated with the specified news article and creation dates before the provided date.
     */
    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateBefore(int newsID, LocalDate beforeCreationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDateBefore(newsID, beforeCreationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    /**
     * Retrieves a list of detailed DTOs (Data Transfer Objects) representing Views associated with a specific news article ID and creation dates after a certain date.
     *
     * @param newsID            The ID of the news article for which associated Views will be retrieved.
     * @param afterCreationDate The lower bound of the creation date range for which associated Views will be retrieved.
     * @return A list of detailed DTOs representing Views associated with the specified news article and creation dates after the provided date.
     */
    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateAfter(int newsID, LocalDate afterCreationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDateAfter(newsID, afterCreationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    /**
     * Retrieves a list of detailed DTOs (Data Transfer Objects) representing Views associated with a specific user ID.
     *
     * @param userID The ID of the user for which associated Views will be retrieved.
     * @return A list of detailed DTOs representing Views associated with the specified user.
     */
    @Override
    public List<ViewDetailedDto> findViewByViewUserID(int userID) {

        List<View> listOfViewsByUserID = viewRepository.findViewByViewUserID(userID);

        return convertListOfViewToViewDetailedDto(listOfViewsByUserID);
    }

    /**
     * Converts a list of View entities to a list of detailed DTOs (Data Transfer Objects).
     *
     * @param listOfViews The list of View entities to be converted.
     * @return A list of detailed DTOs representing the given list of Views.
     */
    private List<ViewDetailedDto> convertListOfViewToViewDetailedDto(List<View> listOfViews) {

        List<ViewDetailedDto> allViewsAsDetailedDto = new ArrayList<>();

        for (View view : listOfViews) {

            allViewsAsDetailedDto.add(fetchViewDetailedDtoData(view));
        }

        return allViewsAsDetailedDto;
    }

    /**
     * Fetches detailed data for a View and constructs a ViewDetailedDto using the retrieved information.
     *
     * @param view The View entity for which detailed data will be fetched.
     * @return A ViewDetailedDto containing detailed information about the provided View.
     * @throws ResourceNotFoundException If the associated News or User for the View is not found.
     */
    private ViewDetailedDto fetchViewDetailedDtoData(View view) {

        UserDto userDto = userService.getUserByID(view.getViewUserID());

        Optional<News> news = newsRepository.findById(view.getViewNewsID());

        News searchedNews = news.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + view.getViewNewsID(), "News"));

        ViewDto viewDto = viewMapper.mapViewToViewDto(view);

        return viewMapper.mapToViewDetailedDto(viewDto, newsMapper.mapNewsToNewsDto(searchedNews), userDto);
    }
}
