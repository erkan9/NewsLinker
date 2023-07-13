package erkamber.services.interfaces;

import erkamber.dtos.NewsTagDetailedDto;
import erkamber.dtos.NewsTagDto;
import erkamber.dtos.TagDto;

import java.util.List;

public interface NewsTagService {

    int addNewTag(NewsTagDto newsTagDto);

    int deleteAllTagsByNewsID(int newsID);

    int deleteNewsTagByTagID(int tagID);

    void deleteNewsTag(int newsID, int tagID);

    List<NewsTagDetailedDto> getTagsOfNews(int newsID);

    List<NewsTagDetailedDto> getNewsOfTag(int tagID);

    List<NewsTagDetailedDto> getAllNewsTags();
}
