package nikos.steamcrawler;

import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SteamCrawlerP2 {

    public static void main(String[] args) {
        ArrayList<String> gameUrlsToCrawl = DBTools.getGameUrlsFromDB();
        int counter = 0;
        String gameUrl = "";
        String response = "";
        String finalString = "";

        System.out.println("gamesToCrawl size: " + gameUrlsToCrawl.size());
        for (int i = 0; i < gameUrlsToCrawl.size(); i++) {

            gameUrl = gameUrlsToCrawl.get(i);
            System.out.println("at: " + i + " url:" + gameUrl);

            Game game = new Game("", "", "", "", "", "", "", "", "", "", "", "", "");
            response = crawlAGame(gameUrl, game);
            if (response.equalsIgnoreCase("ok")) {

                finalString = game.getGameUrl() + "\", \"" + game.getGameName().replaceAll("\"", "'") + "\", \"" + game.getGameDescription().replaceAll("\"", "'")
                        + "\", \"" + game.getGameReviews() + "\", \"" + game.getGameTags()
                        + "\", \"" + game.getReviewsUrl() + "\", \"" + game.getDeveloper().replaceAll("\"", "'") + "\", \"" + game.getPublisher().replaceAll("\"", "'")
                        + "\", \"" + game.getGenres() + "\", \"" + game.getGameIconUrl()
                        + "\", \"" + game.getReleaseDate() + "\", \"" + game.getPrice() + "\", \"" + game.getSupportedLanguages();

                System.out.println(game.toString());
                System.out.println(finalString);
                DBTools.insertToTable(finalString, "gameinfos");
            }
            System.out.println("\n##################################NEXT GAME############################################");
            if (i >= 300) {
                break;
            }

        }
    }

    public static String crawlAGame(String url, Game game) {
        Document doc = null;
        doc = Connect.connect(url);
        if (doc == null) {
            System.out.println("ERROR: null doc at crawlCategory");
            return "null doc";
        }
        String response = doc.html();
        if (response.contains("Please enter your birth date to continue:")) {
            System.out.println("No access in the game due to age verification: " + url);
            return "No access to the page";
        }
        if (response.contains("may not be appropriate for all ages")) {
            System.out.println("No access in the game due to age verification: " + url);
            return "No access to the page";
        }  //Content in this product may not be appropriate for all ages, or may not be appropriate for viewing at work
        String gameDescription = "";
        String gameName = "";
        String releaseDate = "";
        String reviewsUrl = "";
        String developer = "";
        String publisher = "";

        String gameIconUrl = "";
        String price = "";
        ArrayList<String> gameTags = new ArrayList<String>(); //tags einai oi katigories to kathe game opws k sto arxiko search
        ArrayList<String> gameReviews = new ArrayList<String>();
        ArrayList<String> genres = new ArrayList<String>();
        ArrayList<String> supportedLanguages = new ArrayList<String>();

        gameName = getGameName(doc);
        gameDescription = getGameDescription(doc);
        gameReviews = getGameReviews(doc);
        gameTags = getGameTags(doc);
        reviewsUrl = getReviewsUrl(url);
        developer = getDeveloper(doc);
        publisher = getPublisher(doc);
        genres = getGenre(doc);
        gameIconUrl = getIconUrl(doc);
        releaseDate = getReleaseDate(doc);
        price = getPrice(doc);
        supportedLanguages = getSupportedLanguages(doc);

        System.out.println("gameName: " + gameName);
        System.out.println("gameDescription: " + gameDescription);
        System.out.println("developer: " + developer);
        System.out.println("publisher: " + publisher);

        System.out.println("price: " + price);
        System.out.println("releaseDate: " + releaseDate);
        System.out.println("iconUrl: " + gameIconUrl);
        System.out.println("reviewsUrl: " + reviewsUrl);

        System.out.println("-------------------------------------GAME REVIEWS---------------------------------------");
        String finalReviewsString = "";
        for (String review : gameReviews) {
            System.out.println(review);
            if (review.contains("%")) {
                finalReviewsString = review;
                System.out.println("choosen: " + finalReviewsString);
                break;
            }
            finalReviewsString = gameReviews.get(0);

        }
        System.out.println("-------------------------------------END OF REVIEWS-------------------------------------");

        System.out.println("------------------------------------GAME GENRES-----------------------------------------");
        String finalGenreString = "";
        for (String genre : genres) {
            finalGenreString += genre.split("-@-")[0] + "-#-";
            System.out.println(genre);
        }
        if (!finalGenreString.isEmpty()) {
            finalGenreString = finalGenreString.substring(0, finalGenreString.lastIndexOf("-#-"));
        }
        System.out.println("finalGenreString: " + finalGenreString);
        System.out.println("------------------------------------END OF GENRES---------------------------------------");

        System.out.println("------------------------------------GAME TAGS------------------------------------------");
        String finalTagString = "";
        for (String tag : gameTags) {
            finalTagString += tag.split("-@-")[0] + "-#-";
            System.out.println(tag);
        }
        if (!finalTagString.isEmpty()) {
            finalTagString = finalTagString.substring(0, finalTagString.lastIndexOf("-#-"));
        }
        System.out.println("finalTagString: " + finalTagString);
        System.out.println("------------------------------------END OF TAGS----------------------------------------");

        System.out.println("------------------------------------LANGUAGES-----------------------------------------");
        String finalLanguageString = "";
        for (String language : supportedLanguages) {
            finalLanguageString += language + "-#-";
            System.out.println(language);
        }
        if (finalLanguageString.isEmpty()) {
            finalLanguageString = "Unknown";
        } else {
            finalLanguageString = finalLanguageString.substring(0, finalLanguageString.lastIndexOf("-#-"));
        }
        System.out.println("finalLanguageString: " + finalLanguageString);
        System.out.println("----------------------------------END OF LANGUAGES---------------------------------------");

        String finalString = gameName + "-@-" + gameDescription + "-@-" + developer + "-@- " + publisher + "-@-" + price + "-@-" + releaseDate + "-@-" + gameIconUrl + "-@-" + reviewsUrl + "-@-" + finalReviewsString + "-@-" + finalGenreString + "-@-" + finalTagString;
        //game = new Game(url,gameName,gameDescription,finalReviewsString, finalTagString, reviewsUrl, developer, publisher,finalGenreString,gameIconUrl,releaseDate,price,finalLanguageString );
        game.setGameUrl(url);
        game.setGameName(gameName);
        game.setGameDescription(gameDescription);
        game.setGameReviews(finalReviewsString);
        game.setGameTags(finalTagString);
        game.setReviewsUrl(reviewsUrl);
        game.setDeveloper(developer);
        game.setPublisher(publisher);
        game.setGenres(finalGenreString);
        game.setGameIconUrl(gameIconUrl);
        game.setReleaseDate(releaseDate);
        game.setPrice(price);
        game.setSupportedLanguages(finalLanguageString);

        System.out.println("finalString: " + finalString);
        return "ok";
    }

    private static String getGameDescription(Document doc) {
        String gameDescription = null;
        try {
            gameDescription = doc.getElementsByClass("game_description_snippet").first().html();
        } catch (Exception e) {
            return "Unknown";
        }
        if (gameDescription == null) {
            return "Unknown";
        } else {
            if (gameDescription.isEmpty()) {
                return "Unknown";
            }
        }

        return gameDescription;
    }

    private static String getGameName(Document doc) {
        String gameName = null;
        try {
            gameName = doc.getElementsByClass("apphub_AppName").first().html();
        } catch (Exception e) {
            return "Unknown";
        }
        if (gameName == null) {
            return "Unknown";
        } else {
            if (gameName.isEmpty()) {
                return "Unknown";
            }
        }
        return gameName;
    }

    private static ArrayList<String> getSupportedLanguages(Document doc) {
        ArrayList<String> supportedLanguages = new ArrayList<String>();
        Element table = doc.getElementsByClass("game_language_options").first();
        Elements tableRows = table.getElementsByTag("td");
        String allRowData = "";
        String language = "";
        String hasInterface = "";
        String hasFullAudio = "";
        String hasSubtitles = "";
        for (int tr = 0; tr < tableRows.size(); tr += 4) {
            //System.out.println(tr);
            // System.out.println("language: " + tableRows.get(tr).html());
            // System.out.println("hasInterface: " + tableRows.get(tr + 1).html());
            // System.out.println("hasFullAudio: " + tableRows.get(tr + 2).html());
            //System.out.println("hasSubtitles: " + tableRows.get(tr + 3).html());
            language = tableRows.get(tr).html();
            if (tableRows.get(tr + 1).html().contains("<img src=")) {
                hasInterface = "Yes";
            } else {
                hasInterface = "No";
            }
            if (tableRows.get(tr + 2).html().contains("<img src=")) {
                hasFullAudio = "Yes";
            } else {
                hasFullAudio = "No";
            }
            if (tableRows.get(tr + 3).html().contains("<img src=")) {
                hasSubtitles = "Yes";
            } else {
                hasSubtitles = "No";
            }
            //allRowData = language + "-@-" + hasInterface + "-@-" + hasFullAudio + "-@-" + hasSubtitles;
            //supportedLanguages.add(allRowData);
            //System.out.println("allrow: " + allRowData);

            //System.out.println(language);
            supportedLanguages.add(language);

        }

        //System.exit(0);
        return supportedLanguages;
    }

    private static String getPrice(Document doc) {
        String price = "";
        try {
            price = doc.getElementsByClass("game_purchase_price").first().html();
        } catch (Exception e) {
            return "Unknown";
        }
        if (price == null) {
            return "Unknown";
        } else {
            if (price.isEmpty()) {
                return "Unknown";
            }
        }
        return price;
    }

    private static String getReleaseDate(Document doc) {
        String releaseDate = null;
        String temp = "";
        Elements els = doc.getElementsByClass("details_block");
        for (Element block : els) {
            if (block.html().contains("<b>Genre:</b> ")) {
                Element el = block;
                temp = el.html();
            }
        }

        //System.out.println(doc.html());
        try {
            String temp2 = temp.substring(temp.indexOf("<b>Release Date:</b>"));
            releaseDate = temp2.split("</b>")[1].replaceAll("<br>", "").trim();
        } catch (StringIndexOutOfBoundsException e) {
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
        if (releaseDate == null) {
            return "Unknown";
        } else {
            if (releaseDate.isEmpty()) {
                return "Unknown";
            }
        }
        //System.exit(0);
        return releaseDate;
    }

    private static String getIconUrl(Document doc) {
        String iconUrl = null;
        try {
            Elements els = doc.getElementsByClass("apphub_AppIcon");
            String temp = els.html();
            iconUrl = temp.replaceAll("<img src=", "").split(">")[0].replaceAll("\"", "");
            System.out.println("tempurl: " + temp);
        } catch (StringIndexOutOfBoundsException e) {
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }

        if (iconUrl == null) {
            return "Unknown";
        } else {
            if (iconUrl.isEmpty()) {
                return "Unknown";
            }
        }
        //System.out.println(els.html());
        return iconUrl;
    }

    private static ArrayList<String> getGenre(Document doc) {
        ArrayList<String> genres = new ArrayList<String>();
        //System.out.println(doc.html());
        Elements els = doc.getElementsByClass("details_block");
        for (Element block : els) {
            if (block.html().contains("<b>Genre:</b> ")) {
                Element el = block;
                Elements aTags = el.getElementsByTag("a");
                String attrValue = "";
                for (Element tag : aTags) {

                    attrValue = tag.attr("href");
                    if (attrValue.contains("http://store.steampowered.com/genre/")) {
                        //System.out.println(tag);
                        genres.add(tag.html() + "-@-" + attrValue);
                    }
                }
            }
        }

        return genres;
    }

    private static String getPublisher(Document doc) {
        System.out.println("PUBLISHERRRRRRRRR");
        String publisher = "";
        Elements els = doc.getElementsByClass("grid_content");
        System.out.println(els);
        for (Element block : els) {
            System.out.println("########");
            System.out.println(block.html());
            if (block.html().contains("publisher")) {
                publisher = block.html().split(">")[1].replaceAll("</a", "");
            }
        }
        if (publisher.isEmpty()) {
            return "Unknown";
        }
        return publisher;
    }

    private static String getDeveloper(Document doc) {
        String developer = "";
        Element el = doc.getElementById("developers_list");
        developer = el.text();

        if (developer.isEmpty()) {
            return "Unknown";
        }

        return developer;
    }

    private static String getReviewsUrl(String url) {
        String reviewsUrl = url;
        //http://store.steampowered.com/app/10/CounterStrike/?snr=1_7_7_230_150_1
        String[] parts = url.split("app/");
        // System.out.println("parts[0]:" + parts[0]);
        //  System.out.println("parts[1]:" + parts[1]);
        reviewsUrl = "http://steamcommunity.com/app/" + parts[1].split("/")[0] + "/reviews";
        System.out.println("newReviewsUrl: " + reviewsUrl);
        return reviewsUrl;
    }

    private static String getReviewsUrlOld(Document doc) {
        String reviewsUrl = "";
        Elements els = doc.getElementsByClass("details_block");
        for (Element block : els) {
            if (block.html().contains("http://steamcommunity.com/app/")) {
                Element el = block;
                Elements aTags = el.getElementsByTag("a");
                for (Element tag : aTags) {
                    //System.out.println(tag);
                    if (tag.attr("href").contains("http://steamcommunity.com/app/")) {
                        reviewsUrl = tag.attr("href");
                    }
                }
            }
        }
        if (reviewsUrl.isEmpty()) {
            return "Unknown";
        }
//        System.out.println(el.html());
        return reviewsUrl;
    }

    private static ArrayList<String> getGameReviews(Document doc) {
        ArrayList<String> gameReviews = new ArrayList<String>();
        Elements els = doc.getElementsByClass("game_review_summary");
        for (Element e : els) {
//            System.out.println(e.html());
//            System.out.println(e.attr("data-store-tooltip"));
            gameReviews.add(e.html() + "-@-" + e.attr("data-store-tooltip"));
        }
        return gameReviews;
    }

    private static ArrayList<String> getGameTags(Document doc) {
        ArrayList<String> gameTags = new ArrayList<String>(); //tags einai oi katigories to kathe game opws k sto arxiko search
        Elements els = null;
        try {
            els = doc.getElementsByClass("glance_tags popular_tags").first().getElementsByTag("a");
        } catch (Exception e) {
            return gameTags;
        }
        if (els == null) {
            return gameTags;
        }
        for (Element tag : els) {
//            System.out.println(tag.text());
//            System.out.println(tag.attr("href"));
            gameTags.add(tag.text() + "-@-" + tag.attr("href")); //tag-@-tagUrl
        }
        return gameTags;
    }
}
