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

    /**
     * Adds a new media resource based on the provided MediaDto.
     *
     * @param mediaDto The MediaDto containing information about the new media resource.
     * @return The ID of the newly added media resource.
     */
    @Override
    public int addNewMedia(MediaDto mediaDto) {

        Media newMedia = mediaMapper.mapMediaDtoToMedia(mediaDto);

        mediaRepository.save(newMedia);

        return newMedia.getMediaID();

    }

    /**
     * Deletes a media resource based on the specified media ID.
     *
     * @param mediaID The ID of the media resource to be deleted.
     * @throws ResourceNotFoundException If the media resource with the provided ID is not found.
     */
    @Override
    public void deleteMediaByMediaID(int mediaID) {

        Optional<Media> searchedMedia = mediaRepository.findById(mediaID);

        Media searchedMediaObject = searchedMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media not Found: " + mediaID, "Media"));

        mediaRepository.delete(searchedMediaObject);
    }

    /**
     * Deletes all media resources associated with a specific news article based on the provided news article ID.
     *
     * @param newsID The ID of the news article whose associated media resources are to be deleted.
     */
    @Override
    public void deleteMediasByNewsID(int newsID) {

        List<Media> mediasOfNews = mediaRepository.findMediaByMediaNewsID(newsID);

        mediaRepository.deleteAll(mediasOfNews);
    }

    /**
     * Retrieves a media resource based on the specified media string.
     *
     * @param mediaString The media string to search for.
     * @return A {@link MediaDto} object representing the media resource.
     * @throws ResourceNotFoundException If the media resource with the provided string is not found.
     */
    @Override
    public MediaDto findMediaByString(String mediaString) {

        Optional<Media> searchedMedia = mediaRepository.findMediaByMediaString(mediaString);

        Media searchedMediaObject = searchedMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media not Found", "Media"));

        return mediaMapper.mapMediaToMediaDto(searchedMediaObject);
    }

    /**
     * Retrieves a media resource based on the specified media ID.
     *
     * @param mediaID The ID of the media resource to be retrieved.
     * @return A {@link MediaDto} object representing the media resource.
     * @throws ResourceNotFoundException If the media resource with the provided ID is not found.
     */
    @Override
    public MediaDto findMediaByMediaID(int mediaID) {

        Optional<Media> searchedMedia = mediaRepository.findById(mediaID);

        Media searchedMediaObject = searchedMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media not Found: " + mediaID, "Media"));

        return mediaMapper.mapMediaToMediaDto(searchedMediaObject);
    }

    /**
     * Retrieves a list of media resources associated with a specific news article based on the provided news article ID.
     *
     * @param newsID The ID of the news article whose associated media resources are to be retrieved.
     * @return A list of {@link MediaDto} objects representing the media resources associated with the news article.
     */
    @Override
    public List<MediaDto> findMediaByNewsID(int newsID) {

        List<Media> searchedMediaByNewsID = mediaRepository.findMediaByMediaNewsID(newsID);

        return mediaMapper.mapListOfMediaToMediaDto(searchedMediaByNewsID);
    }

    /**
     * Retrieves a list of all media resources.
     *
     * @return A list of {@link MediaDto} objects representing all media resources.
     */
    @Override
    public List<MediaDto> getAllMedias() {

        List<Media> listOfAllMedias = mediaRepository.findAll();

        return mediaMapper.mapListOfMediaToMediaDto(listOfAllMedias);
    }
}
