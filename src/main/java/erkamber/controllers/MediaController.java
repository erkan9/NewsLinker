package erkamber.controllers;

import erkamber.configurations.HttpHeadersConfiguration;
import erkamber.dtos.MediaDto;
import erkamber.services.interfaces.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class MediaController {

    private final MediaService mediaService;

    private final HttpHeadersConfiguration headers;

    public MediaController(MediaService mediaService, HttpHeadersConfiguration headers) {
        this.mediaService = mediaService;
        this.headers = headers;
    }

    @PostMapping("/medias")
    public ResponseEntity<Void> addNewMedia(@Valid @RequestBody MediaDto newMediaDto) {

        int newMediaDtoID = mediaService.addNewMedia(newMediaDto);

        headers.getHeaders().set("NewMediaID", String.valueOf(newMediaDtoID));

        return new ResponseEntity<>(headers.getHeaders(), HttpStatus.CREATED);
    }

    @GetMapping("/medias")
    public ResponseEntity<List<MediaDto>> getAllMedias() {

        return ResponseEntity.ok(mediaService.getAllMedias());
    }

    @GetMapping("/medias/{mediaID}")
    public ResponseEntity<MediaDto> getMediaByID(@PathVariable int mediaID) {

        return ResponseEntity.ok(mediaService.findMediaByMediaID(mediaID));
    }

    @GetMapping(value = "/medias", params = {"newsId"})
    public ResponseEntity<List<MediaDto>> getMediaByNewsID(@RequestParam("newsId")
                                                           @Positive(message = "News ID must be a Positive number!")
                                                           int newsID) {

        return ResponseEntity.ok(mediaService.findMediaByNewsID(newsID));
    }

    @GetMapping(value = "/medias", params = {"mediaString"})
    public ResponseEntity<MediaDto> getMediaByMediaString(@RequestParam("mediaString")
                                                          @NotBlank(message = "Media String cannot be Blank!")
                                                          @NotEmpty(message = "Media String cannot be Empty!")
                                                          @NotNull(message = "Media String cannot be null!")
                                                          String mediaString) {

        return ResponseEntity.ok(mediaService.findMediaByString(mediaString));
    }

    @DeleteMapping(value = "/medias", params = {"mediaId"})
    public void deleteMediaByMediaID(@RequestParam("mediaId")
                                     @Positive(message = "Media ID must be a Positive number!")
                                     int mediaID) {

        mediaService.deleteMediaByMediaID(mediaID);
    }

    @DeleteMapping(value = "/medias", params = {"newsId"})
    public void deleteMediasByNewsID(@RequestParam("newsId")
                                     @Positive(message = "News ID must be a Positive number!")
                                     int newsID) {

        mediaService.deleteMediasByNewsID(newsID);
    }
}
