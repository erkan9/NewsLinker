package erkamber.services.implementations;

import erkamber.dtos.MediaDto;
import erkamber.entities.Media;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.MediaMapper;
import erkamber.repositories.MediaRepository;
import erkamber.services.interfaces.MediaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaServiceImpl(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }


    @Override
    public int addNewMedia(MediaDto mediaDto) {

        Media newMedia = mediaMapper.mapMediaDtoToMedia(mediaDto);

        mediaRepository.save(newMedia);

        return newMedia.getMediaID();

    }

    @Override
    public void deleteMediaByMediaID(int mediaID) {

        Optional<Media> searchedMedia = mediaRepository.findById(mediaID);

        Media searchedMediaObject = searchedMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media not Found: " + mediaID, "Media"));

        mediaRepository.delete(searchedMediaObject);
    }

    @Override
    public void deleteMediasByNewsID(int newsID) {

        List<Media> mediasOfNews = mediaRepository.findMediaByMediaNewsID(newsID);

        mediaRepository.deleteAll(mediasOfNews);
    }

    @Override
    public MediaDto findMediaByString(String mediaString) {

        Optional<Media> searchedMedia = mediaRepository.findMediaByMediaString(mediaString);

        Media searchedMediaObject = searchedMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media not Found", "Media"));

        return mediaMapper.mapMediaToMediaDto(searchedMediaObject);
    }

    @Override
    public MediaDto findMediaByMediaID(int mediaID) {

        Optional<Media> searchedMedia = mediaRepository.findById(mediaID);

        Media searchedMediaObject = searchedMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media not Found: " + mediaID, "Media"));

        return mediaMapper.mapMediaToMediaDto(searchedMediaObject);
    }

    @Override
    public List<MediaDto> findMediaByNewsID(int newsID) {

        List<Media> searchedMediaByNewsID = mediaRepository.findMediaByMediaNewsID(newsID);

        return mediaMapper.mapListOfMediaToMediaDto(searchedMediaByNewsID);
    }

    @Override
    public List<MediaDto> getAllMedias() {

        List<Media> listOfAllMedias = mediaRepository.findAll();

        return mediaMapper.mapListOfMediaToMediaDto(listOfAllMedias);
    }
}
