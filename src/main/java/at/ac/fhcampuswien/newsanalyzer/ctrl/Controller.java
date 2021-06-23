package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsanalyzer.downloader.Downloader;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsanalyzer.ctrl.NewsAPIException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.beans.Source;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.SortBy;
import at.ac.fhcampuswien.newsanalyzer.downloader.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

	public static final String APIKEY = "30a1fa1918d2482db311a48485b7d446";

	private Country country;
	private String keyword;
	private Category category;
	private SortBy sortBy;

	private NewsResponse newsResponse;

	List<Article> articles;


	public void process() {

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ(this.keyword) //				.setEndPoint(Endpoint.EVERYTHING)
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setFrom("2021-06-20")
				.setSourceCountry(this.country)
				.setSourceCategory(this.category)
				.setSortBy(this.sortBy)
				.createNewsApi();
		newsResponse = newsApi.getNews();
	}

	public void setSortBy(SortBy sortBy){
		this.sortBy = sortBy;
	}

	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	public void setCountry(Country country){
		this.country = country;
	}

	public void setCategory(Category category){
		this.category = category;
	}

	String authorWithShortestName;
	int numberOfArticles;
	List<String> urls = new ArrayList<>();

	public void analysis(NewsResponse newsResponse){

		if(newsResponse != null){

			System.out.println("\nHier die Ergebnisse unserer hochentwickelten Analysesoftware: \n");

			articles = newsResponse.getArticles();

			//Number of Articles
			numberOfArticles = articles.size();
			System.out.println("Anzahl der Artikel: " + numberOfArticles + "\n");

			//Which provider delivers the most articles
			String providerWithMostArticles = articles.stream()
					.map(Article::getSource)
					.collect(Collectors.groupingBy(Source::getName))
					.entrySet()
					.stream()
					.max(Comparator.comparingInt(o -> o.getValue().size()))
					.map(stringListEntry -> stringListEntry.getKey() + " " + stringListEntry.getValue().size())
					.orElseThrow();

			System.out.println("Die Quelle mit den meisten Artikeln ist: " + providerWithMostArticles + "\n");

			//Author with Shortest Name
			authorWithShortestName = articles.stream()
					.map(Article::getAuthor)
					.filter(Objects::nonNull)
					.min(Comparator.comparing(String::length))
					.orElseThrow();

			System.out.println("Der Autor mit dem kürzesten Namen ist: " + authorWithShortestName + "\n");

			//sort for length of articles
			System.out.println("Hier die Artikel der Länge nach sortiert: \n" +
					articles.stream()
							.map(Article::getTitle)
							.filter(Objects::nonNull)
							.sorted(Comparator.comparing(String::length).reversed())
							.collect(Collectors.joining("\n")));

			//URLs als Liste zusammenfassen
			urls = articles.stream().map(Article::getUrl).filter(Objects::nonNull).collect(Collectors.toList());
		}
	}

	public NewsResponse getData() {
		return newsResponse;
	}
}


