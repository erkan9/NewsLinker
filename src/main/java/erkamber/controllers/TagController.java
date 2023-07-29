package erkamber.controllers;


import erkamber.configurations.HttpHeadersConfiguration;
import erkamber.dtos.TagDto;
import erkamber.services.interfaces.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class TagController {

    private final TagService tagService;

    private final HttpHeadersConfiguration httpHeadersConfiguration;

    public TagController(TagService tagService, HttpHeadersConfiguration httpHeadersConfiguration) {
        this.tagService = tagService;
        this.httpHeadersConfiguration = httpHeadersConfiguration;
    }

    @PostMapping("/tags")
    public ResponseEntity<Void> addNewTag(@Valid @RequestBody TagDto newTagDto) {

        int newTagID = tagService.addNewTag(newTagDto);

        httpHeadersConfiguration.getHeaders().set("TagID", String.valueOf(newTagID));

        return new ResponseEntity<>(httpHeadersConfiguration.getHeaders(), HttpStatus.CREATED);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> getAllTags() {

        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<TagDto> getTagByTagID(@PathVariable int tagId) {

        return ResponseEntity.ok(tagService.findTagByTagID(tagId));
    }

    @GetMapping(value = "/tags", params = {"tagName"})
    public ResponseEntity<TagDto> getTagByTagName(@RequestParam("tagName")
                                                  @NotBlank(message = "Tag Name cannot be Blank!")
                                                  @NotEmpty(message = "Tag Name cannot be Empty!")
                                                  @NotNull(message = "Tag Name cannot be null!")
                                                  @Size(max = 40, message = "Tag Name must be up to 40 characters long")
                                                  String tagName) {

        return ResponseEntity.ok(tagService.findTagByTagName(tagName));
    }

    @DeleteMapping(value = "/tags", params = {"tagId"})
    public void deleteTagByTagID(@RequestParam("tagId")
                                 @Positive(message = "Tag ID must be a Positive number!")
                                 int tagId) {

        tagService.deleteTagByTagID(tagId);
    }

    @DeleteMapping(value = "/tags", params = {"tagName"})
    public void deleteTagByTagName(@RequestParam("tagName")
                                   @NotBlank(message = "Tag Name cannot be Blank!")
                                   @NotEmpty(message = "Tag Name cannot be Empty!")
                                   @NotNull(message = "Tag Name cannot be null!")
                                   @Size(max = 40, message = "Tag Name must be up to 40 characters long")
                                   String tagName) {

        tagService.deleteTagByTagName(tagName);
    }

    @PatchMapping(value = "/tags", params = {"tagName"})
    public void updateTagByTagName(@RequestParam(name = "tagName")
                                   @NotBlank(message = "Tag Name cannot be Blank!")
                                   @NotEmpty(message = "Tag Name cannot be Empty!")
                                   @NotNull(message = "Tag Name cannot be null!")
                                   @Size(max = 40, message = "Tag Name must be up to 40 characters long")
                                   String tagName,
                                   @Valid @RequestBody
                                   TagDto tagDto) {

        tagService.updateTagByTagName(tagName, tagDto.getTagName());
    }

    @PatchMapping(value = "/tags", params = {"tagId"})
    public void updateTag(@RequestParam("tagId")
                          @Positive(message = "Tag ID must be a Positive number!")
                          int tagId,
                          @Valid @RequestBody
                          TagDto tagDto) {

        tagService.updateTag(tagId, tagDto);
    }
}