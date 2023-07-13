package erkamber.mappers;

import erkamber.dtos.*;
import erkamber.entities.NewsTag;
import org.springframework.stereotype.Component;

@Component
public class NewsTagMapper {

    public NewsTagDto mapNewsTagToNewsTagDto(NewsTag newsTag) {

        return new NewsTagDto(newsTag.getNewsTagID(), newsTag.getNewsID(), newsTag.getTagID());
    }

    public NewsTag mapNewsTagToNewsTagDto(NewsTagDto newsTagDto) {

        return new NewsTag(newsTagDto.getNewsTagID(), newsTagDto.getNewsID(), newsTagDto.getTagID());
    }

    public NewsTagDetailedDto mapToNewsTagDetailedDto(int newsTagID, NewsDetailedDto newsDetailedDto, TagDto tagDto) {

        return new NewsTagDetailedDto(newsTagID, newsDetailedDto, tagDto);
    }
}
