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

    public NewsDetailedDto mapToNewsDtoDetailed(NewsDto newsDto, UserDto userDto, int numberOfViews, List<TagDto> listOfNewsTags,
                                                List<MediaDto> listOfNewsMedia, List<CommentDetailedDto> listOfComments,
                                                List<VoteDto> listOfUpVotes, List<VoteDto> listOfDownVotes) {

        return new NewsDetailedDto(newsDto.getNewsID(), userDto, newsDto.getNewsTitle(), newsDto.getNewsContent(),
                newsDto.getNewsUpVotes(), newsDto.getNewsDownVotes(), numberOfViews, newsDto.getNewsCreationDate(),
                listOfNewsTags, listOfNewsMedia, listOfComments, listOfUpVotes, listOfDownVotes);
    }

    public List<NewsDto> mapListOfNewsToNewsDto(List<News> listOfNews) {

        return listOfNews.stream().map(this::mapNewsToNewsDto).collect(Collectors.toList());
    }
}
