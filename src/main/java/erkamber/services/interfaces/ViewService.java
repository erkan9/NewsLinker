package erkamber.services.interfaces;

import erkamber.dtos.ViewDetailedDto;
import erkamber.dtos.ViewDto;

import java.time.LocalDate;
import java.util.List;

public interface ViewService {

    int addNewView(ViewDto viewDto);

    void deleteViewsByViewID(int viewID);

    void deleteViewsByUserID(int userID);

    void deleteViewsByNewsID(int newsID);

    ViewDetailedDto getViewByID(int viewID);

    List<ViewDetailedDto> getAllViews();

    List<ViewDetailedDto> findViewByViewNewsID(int newsID);

    List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDate(int newsID, LocalDate creationDate);

    List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateBefore(int newsID, LocalDate beforeCreationDate);

    List<ViewDetailedDto> findViewByViewNewsIDAndViewCreationDateAfter(int newsID, LocalDate afterCreationDate);

    List<ViewDetailedDto> findViewByViewUserID(int userID);
}
