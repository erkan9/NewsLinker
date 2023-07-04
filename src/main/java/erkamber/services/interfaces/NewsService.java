package erkamber.services.interfaces;

import erkamber.dtos.NewsDto;

import java.time.LocalDate;
import java.util.List;

public interface NewsService {

    NewsDto getNewsByNewsID(int newsID);

    NewsDto getNews(int newsID, int userID);

    int addNews(NewsDto newsDto);

    int updateNews(NewsDto newsDto, int userID);

    int updateNewsContent(String content, int newsID, int userID);

    int updateNewsTitle(String title, int newsID, int userID);

    NewsDto findNewsByNewsID(int newsID);

    List<NewsDto> getAllNews();

    List<NewsDto> findNewsByUserID(int userID);

    List<NewsDto> findNewsByUserName(String userName);

    List<NewsDto> findNewsByNewsTitle(String newsTitle);

    List<NewsDto> findNewsByUpVotesLowerThan(int upVotesCount);

    List<NewsDto> findNewsByUpVotesGreaterThan(int upVotesCount);

    List<NewsDto> findNewsByDownVotesLowerThan(int downVotesCount);

    List<NewsDto> findNewsByDownVotesGreaterThan(int downVotesCount);

    List<NewsDto> findNewsByCreationDateBefore(LocalDate beforeDate);

    List<NewsDto> findNewsByCreationDateAfter(LocalDate afterDate);

    List<NewsDto> findNewsByCreationBetween(LocalDate startDate, LocalDate endDate);

    List<NewsDto> findNewsByContentContaining(String content);
}
