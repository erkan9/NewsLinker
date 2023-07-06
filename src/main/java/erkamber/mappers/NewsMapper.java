package erkamber.mappers;

import erkamber.dtos.*;
import erkamber.entities.News;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsMapper {

    public NewsDto mapNewsToNewsDto(News news) {

        return new NewsDto(news.getNewsID(), news.getUserID(), news.getNewsTitle(), news.getNewsContent(),
                news.getNewsUpVotes(), news.getNewsDownVotes(), news.getNewsCreationDate());
    }

    public News mapNewsDtoToNews(NewsDto newsDto) {

        return new News(newsDto.getNewsID(), newsDto.getUserID(), newsDto.getNewsTitle(), newsDto.getNewsContent(),
                newsDto.getNewsUpVotes(), newsDto.getNewsDownVotes(), newsDto.getNewsCreationDate());
    }

    public NewsDetailedDto mapNewsToNewsDtoDetailed(NewsDto newsDto, UserDto userDto,
                                                    List<TagDto> listOfNewsTags, List<MediaDto> listOfNewsMedia) {

        return new NewsDetailedDto(newsDto.getNewsID(), userDto, newsDto.getNewsTitle(), newsDto.getNewsContent(),
                newsDto.getNewsUpVotes(), newsDto.getNewsDownVotes(), newsDto.getNewsCreationDate(),
                listOfNewsTags, listOfNewsMedia);
    }

    public List<NewsDto> mapListOfNewsToNewsDto(List<News> listOfNews) {

        return listOfNews.stream().map(this::mapNewsToNewsDto).collect(Collectors.toList());
    }
}
