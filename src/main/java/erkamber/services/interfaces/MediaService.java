package erkamber.services.interfaces;

import erkamber.dtos.MediaDto;
import erkamber.entities.Media;

import java.util.List;

public interface MediaService {

    int addMedia(MediaDto mediaDto);

    void deleteMediaByMediaID(int mediaID);

    void deleteMediasByNewsID(int newsID);

    MediaDto findMediaByString(String mediaString);

    List<MediaDto> findMediaByNewsID(int newsID);

    List<MediaDto> getAllMedias();
}
