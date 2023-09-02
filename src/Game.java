package nikos.steamcrawler;

public class Game {

    private String gameUrl;
    private String gameName;
    private String gameDescription;
    private String gameReviews;
    private String gameTags;
    private String reviewsUrl;
    private String developer;
    private String publisher;
    private String genres;
    private String gameIconUrl;
    private String releaseDate;
    private String price;
    private String supportedLanguages;

    public Game(String gameUrl, String gameName, String gameDescription, String gameReviews, String gameTags, String reviewsUrl,
            String developer, String publisher, String genres, String gameIconUrl,String releaseDate,String price, String supportedLanguages) {
        this.gameUrl = gameUrl;
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.gameReviews = gameReviews;
        this.gameTags = gameTags;
        this.reviewsUrl = reviewsUrl;
        this.developer = developer;
        this.publisher = publisher;
        this.genres = genres;
        this.gameIconUrl = gameIconUrl;
        this.releaseDate = releaseDate;
        this.price = price;
        this.supportedLanguages = supportedLanguages;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public String getGameReviews() {
        return gameReviews;
    }

    public String getGameTags() {
        return gameTags;
    }

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getGenres() {
        return genres;
    }

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPrice() {
        return price;
    }

    public String getSupportedLanguages() {
        return supportedLanguages;
    }

    @Override
    public String toString() {
        return "Game{\n" + "   gameUrl=" + gameUrl + "\n   gameName=" + gameName + "\n   gameDescription=" + gameDescription + "\n   gameReviews=" + gameReviews + "\n   gameTags=" + gameTags + "\n   reviewsUrl=" + reviewsUrl + "\n   developer=" + developer + "\n   publisher=" + publisher + "\n   genres=" + genres + "\n   gameIconUrl=" + gameIconUrl + "\n   releaseDate=" + releaseDate + "\n   price=" + price + "\n   supportedLanguages=" + supportedLanguages + "\n}";
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public void setGameReviews(String gameReviews) {
        this.gameReviews = gameReviews;
    }

    public void setGameTags(String gameTags) {
        this.gameTags = gameTags;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }
    
    
}
