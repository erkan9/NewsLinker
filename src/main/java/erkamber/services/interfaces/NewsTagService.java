package erkamber.services.interfaces;

import erkamber.dtos.NewsTagDetailedDto;
import erkamber.dtos.NewsTagDto;

import java.util.List;

public interface NewsTagService {


    int addNewsTag(NewsTagDto newsTagDto);

    int deleteAllTagsByNewsID(int newsID);

    int deleteNewsTagByTagID(int tagID);

    void deleteNewsTag(int newsID, int tagID);

    NewsTagDto getNewsTagByID(int newsTagID);

    List<NewsTagDetailedDto> getTagsOfNewsDetailed(int newsID);

    List<NewsTagDto> getTagsOfNews(int newsID);

    List<NewsTagDetailedDto> getNewsOfTag(int tagID);

    List<NewsTagDetailedDto> getAllNewsTags();
}
