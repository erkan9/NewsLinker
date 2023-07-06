package erkamber.services.implementations;

import erkamber.dtos.NewsDto;
import erkamber.dtos.UserDto;
import erkamber.dtos.ViewDetailedDto;
import erkamber.dtos.ViewDto;
import erkamber.entities.View;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.ViewMapper;
import erkamber.repositories.ViewRepository;
import erkamber.services.interfaces.NewsService;
import erkamber.services.interfaces.UserService;
import erkamber.services.interfaces.ViewService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class ViewServiceImpl implements ViewService {

    private final ViewMapper viewMapper;

    private final ViewRepository viewRepository;

    private final UserService userService;

    private final NewsService newsService;

    public ViewServiceImpl(ViewMapper viewMapper, ViewRepository viewRepository, UserService userService, NewsService newsService) {
        this.viewMapper = viewMapper;
        this.viewRepository = viewRepository;
        this.userService = userService;
        this.newsService = newsService;
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

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDate(newsID, beforeCreationDate);

        return convertListOfViewToViewDetailedDto(viewsByNewsIDAndCreationDate);
    }

    @Override
    public List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateAfter(int newsID, LocalDate afterCreationDate) {

        List<View> viewsByNewsIDAndCreationDate = viewRepository.findViewByViewNewsIDAndViewCreationDate(newsID, afterCreationDate);

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

        NewsDto newsDto = newsService.getNewsByNewsID(view.getViewNewsID());

        ViewDto viewDto = viewMapper.mapViewToViewDto(view);

        return viewMapper.mapToViewDetailedDto(viewDto, newsDto, userDto);
    }
}
