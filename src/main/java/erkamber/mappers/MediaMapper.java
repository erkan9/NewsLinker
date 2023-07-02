package erkamber.mappers;

import erkamber.dtos.MediaDto;
import erkamber.entities.Media;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MediaMapper {

    public MediaDto mapMediaToMediaDto(Media media) {

        return new MediaDto(media.getMediaID(), media.getMediaNewsID(), media.getMediaString());
    }

    public Media mapMediaDtoToMedia(MediaDto mediaDto) {

        return new Media(mediaDto.getMediaID(), mediaDto.getNewsID(), mediaDto.getMediaString());
    }

    public List<MediaDto> mapListOfMediaToMediaDto(List<Media> listOfMedia) {

        return listOfMedia.stream().map(this::mapMediaToMediaDto).collect(Collectors.toList());
    }
}
