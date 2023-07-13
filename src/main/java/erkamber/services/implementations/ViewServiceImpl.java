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

    @Override
    public void deleteViewsByViewID(int viewID) {

        View view = viewRepository.findById(viewID)
                .orElseThrow(() -> new ResourceNotFoundException("View with that ID, could not be found", "View"));

        viewRepository.deleteById(viewID);
    }

    @Override
    public void deleteViewsByUserID(int userID) {

        List<View> viewsByUserID = viewRepository.findViewByViewUserID(userID);

        viewRepository.deleteAll(viewsByUserID);
    }

    @Override
    public void deleteViewsByNewsID(int newsID) {

        List<View> viewsByNewsID = viewRepository.findViewByViewNewsID(newsID);

        viewRepository.deleteAll(viewsByNewsID);
    }

    @Override
    public int addNewView(ViewDto viewDto) {

        View newView = viewMapper.mapViewDtoToView(viewDto);

        viewRepository.save(newView);

        return newView.getViewID();
    }

    @Override
    public int getNumberOfViewsOfNews(int newsID) {

        List<View> viewsByNewsID = viewRepository.findViewByViewNewsID(newsID);

        return viewsByNewsID.size();
    }

    @Override
    public ViewDetailedDto getViewByID(int viewID) {

        View view = viewRepository.findById(viewID)
                .orElseThrow(() -> new ResourceNotFoundException("View with that ID, could not be found", "View"));

        return fetchViewDetailedDtoData(view);
    }

    @Override
    public List<ViewDetailedDto> getAllViews() {

        List<View> listOfAllViews = viewRepository.findAll();

        return convertListOfViewToViewDetailedDto(listOfAllViews);
    }

    @Override
    public List<ViewDetailedDto> findViewByViewNewsID(int newsID) {

        List<View> viewsByNewsID = viewRepository.findViewByViewNewsID(newsID);

        return convertListOfViewToViewDetailedDto(viewsByNewsID);
    }

    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDate(int newsID, LocalDate creationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDate(newsID, creationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateBefore(int newsID, LocalDate beforeCreationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDateBefore(newsID, beforeCreationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateAfter(int newsID, LocalDate afterCreationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDateAfter(newsID, afterCreationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    @Override
    public List<ViewDetailedDto> findViewByViewUserID(int userID) {

        List<View> listOfViewsByUserID = viewRepository.findViewByViewUserID(userID);

        return convertListOfViewToViewDetailedDto(listOfViewsByUserID);
    }

    private List<ViewDetailedDto> convertListOfViewToViewDetailedDto(List<View> listOfViews) {

        List<ViewDetailedDto> allViewsAsDetailedDto = new ArrayList<>();

        for (View view : listOfViews) {

            allViewsAsDetailedDto.add(fetchViewDetailedDtoData(view));
        }

        return allViewsAsDetailedDto;
    }

    private ViewDetailedDto fetchViewDetailedDtoData(View view) {

        UserDto userDto = userService.getUserByID(view.getViewUserID());

        Optional<News> news = newsRepository.findById(view.getViewNewsID());

        News searchedNews = news.orElseThrow(() ->
                new ResourceNotFoundException("News not Found: " + view.getViewNewsID(), "News"));

        ViewDto viewDto = viewMapper.mapViewToViewDto(view);

        return viewMapper.mapToViewDetailedDto(viewDto, newsMapper.mapNewsToNewsDto(searchedNews), userDto);
    }
}
