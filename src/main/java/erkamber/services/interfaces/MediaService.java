package erkamber.services.interfaces;

import erkamber.dtos.MediaDto;

import java.util.List;

public interface MediaService {

    int addNewMedia(MediaDto mediaDto);

    void deleteMediaByMediaID(int mediaID);

    void deleteMediasByNewsID(int newsID);

    MediaDto findMediaByString(String mediaString);

    MediaDto findMediaByMediaID(int mediaID);

    List<MediaDto> findMediaByNewsID(int newsID);

    List<MediaDto> getAllMedias();
}
