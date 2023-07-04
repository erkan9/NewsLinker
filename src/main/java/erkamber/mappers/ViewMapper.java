package erkamber.mappers;


import erkamber.dtos.NewsDto;
import erkamber.dtos.UserDto;
import erkamber.dtos.ViewDetailedDto;
import erkamber.dtos.ViewDto;
import erkamber.entities.View;
import org.springframework.stereotype.Component;

@Component
public class ViewMapper {

    public ViewDto mapViewToViewDto(View view) {

        return new ViewDto(view.getViewID(), view.getViewCreationDate(), view.getViewNewsID(), view.getViewUserID());
    }

    public View mapViewDtoToView(ViewDto viewDto) {

        return new View(viewDto.getViewID(), viewDto.getViewCreationDate(), viewDto.getViewNewsID(), viewDto.getViewUserID());
    }

    public ViewDetailedDto mapToViewDetailedDto(ViewDto view, NewsDto newsDto, UserDto userDto) {

        return new ViewDetailedDto(view.getViewID(), view.getViewCreationDate(), newsDto, userDto);
    }

}
