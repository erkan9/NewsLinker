package erkamber.services.interfaces;

import erkamber.dtos.NewsDetailedDto;
import erkamber.dtos.NewsDto;

import java.time.LocalDate;
import java.util.List;

public interface NewsService {

    int addNews(NewsDto newsDto);

    void updateNewsContent(String content, int newsID, int userID);

    void updateNewsTitle(String title, int newsID, int userID);

    void deleteNewsByID(int newsID);

    void deleteNewsByAuthorID(int authorID);

    NewsDto getNewsByNewsID(int newsID);

    NewsDetailedDto getNewsDetailedByNewsID(int newsID);

    NewsDetailedDto getNewsAsLoggedUser(int userID, int newsID);

    List<NewsDetailedDto> getAllNews();

    List<NewsDetailedDto> findNewsByUserID(int userID);

    List<NewsDetailedDto> findNewsByNewsTitle(String newsTitle);

    List<NewsDetailedDto> findNewsByCreationDateBefore(LocalDate beforeDate);

    List<NewsDetailedDto> findNewsByCreationDateAfter(LocalDate afterDate);

    List<NewsDetailedDto> getTopFourTrendingNews(int countOfTrendingNews);
}
