package nikos.steamcrawler;

import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SteamCrawlerP1 {

    public static void main(String[] args) {
        if (DBTools.tableIsEmpty("steamgametagurls")) {
            getGameTagsAndIDs();
        } else {
            System.out.println("game tags already in DB, continue to crawl");
        }
        startCrawling();
    }

    private static void startCrawling() {
        ArrayList<String> ids = DBTools.getTheContentsOfTable("gameID", "steamgametagurls");
        int counter = 0;
        for (String id : ids) {      
            crawlCategory(id);
        }
    }

    private static int crawlCategory(String id) {
        ArrayList<String> gameInfos = new ArrayList<String>();

        int maxPage = 0;

        String url = "http://store.steampowered.com/search/?tags=" + id + "&category1=998&page=1";
        System.out.println("starting: " + url);
        Document doc = null;
        doc = Connect.connect(url);
        if (doc == null) {
            System.out.println("ERROR: null doc at crawlCategory");
        }

        maxPage = calculateMaxPage(doc);
        if (maxPage == 0) {
            System.out.println("skipping category because maxPage = 0");
            return 0;
        }
      
        String finalString = "";
        String gameUrl = "";
        String gameName = "";
        String[] parts;
        for (int page = 1; page <= maxPage; page++) {
            System.out.println("\ncrawling: " + "http://store.steampowered.com/search/?tags=" + id + "&category1=998&page=" + page);
            gameInfos = getGameInfosFromTagSearch("http://store.steampowered.com/search/?tags=" + id + "&category1=998&page=" + page);
            for (String s : gameInfos) {
                parts = s.split("-@-");
                try{
                gameUrl = parts[0];
                gameName = parts[1];    
                } catch (Exception e){
                    continue;
                }
                finalString = gameUrl + "\", \"" + gameName.replaceAll("\"","'");
                DBTools.insertToTable(finalString, "steamGameUrlsAndtitles");
            }
            //DBTools.saveGameInfos(gameInfos);
        }

        System.out.println("------------------------------------------------");

        return 1;
    }

    private static ArrayList<String> getGameInfosFromTagSearch(String url) {
        ArrayList<String> gameInfos = new ArrayList<String>();
        Document doc = null;
        doc = Connect.connect(url);
        if (doc == null) {
            System.out.println("ERROR: null doc at getGameInfosFromTagSearch");
        }

        Element e = doc.getElementById("search_result_container");
        Elements tableRows = e.getElementsByTag("a");
        System.out.println("tablesize: " + tableRows.size());
        String gameUrl = "";
        String gameTitle = "";
       
        Elements els;
        for (Element el : tableRows) {
            els = el.getAllElements();
            gameUrl = els.get(0).attr("href");
            gameTitle = el.getElementsByClass("title").html();
            if (gameUrl.contains("http://store.steampowered.com/search/?sort_by=&sort_order=0&category1=998&special_categories=&tags=")) {
                continue;
            }
            // System.out.println("gameUrl:" + gameUrl);
            //System.out.println("gameTitle: " + gameTitle);
            gameInfos.add(gameUrl + "-@-" + gameTitle);
            /*imageUrl = el.getElementsByTag("img").attr("src");            
            review = getAverageReview(el.getElementsByClass("search_review_summary"));
            platforms = el.getElementsByTag("span").get(1).attr("class");
            releasedDate = el.getElementsByClass("search_released").html();
            cost = el.getElementsByClass("search_price").html();             
            System.out.println("imageUrl: " + imageUrl);
            System.out.println("review: " + review);
            System.out.println("platforms: " + platforms);
            System.out.println("date: " + releasedDate);
            System.out.println("cost: " + cost);
            System.out.println("-----------------------------------------------"); */

        }
        return gameInfos;
    }

    private static String getRowInfos(Element el) {
        return "";
    }

    private static String getAverageReview(Elements els) {
        String review = "";
        String[] parts = els.outerHtml().split("<br>");
        try {
            review = parts[1].split("\">")[0];
        } catch (Exception e) {
            System.out.println("error at getAverageReview");
        }

        return review;
    }

    private static int calculateMaxPage(Document doc) {
        Elements els = doc.getElementsByClass("search_pagination_right");
        try {
            Elements aTags = els.first().getElementsByTag("a");

            int size = aTags.size();
            System.out.println("size:" + size);
            int maxPage = 0;
            if (size == 0) {
                maxPage = 1;
            } else {
                maxPage = Integer.valueOf(aTags.get(size - 2).html());
            }
            System.out.println("maxPage: " + maxPage);
            return maxPage;
        } catch (Exception e) {
            System.out.println("error in calculateMaxPage");
            return 0;
        }

    }

    private static void getGameTagsAndIDs() {
        ArrayList<String> tagUrlsAndIDs = getGameTagUrlsAndIDs("http://store.steampowered.com/tag/browse/#global_492");
        String url = "";
        String id = "";
        String category = "";
        String[] parts;
        String finalString = "";
        for (String s : tagUrlsAndIDs) {

            parts = s.split("-@-");
            url = parts[0];
            id = parts[1];
            category = parts[2];
            finalString = url + "\", \"" + id + "\", \"" + category;
            DBTools.insertToTable(finalString, "steamgametagurls");

        }
    }

    private static ArrayList<String> getGameTagUrlsAndIDs(String url) {
        Document doc = null;
        doc = Connect.connect(url);
        if (doc == null) {
            System.out.println("ERROR: null doc at getGameTagUrls");
        }
        ArrayList<String> tagUrlsAndIDs = new ArrayList<String>();
        Element e = doc.getElementById("tag_browse_global"); //get the div that contains all the tags

        Elements els = e.getAllElements();
        String generalUrl = "http://store.steampowered.com/tag/en/";
        String category = "";
        String tagUrl = "";
        String finalString = "";
        els.remove(0); // ekane print k 1 akyro part opote to evgala
        for (Element el : els) {
            String categoryID = el.attr("data-tagid");
            category = el.html();
            if (category.contains("&amp")) {
                category = category.replaceAll("&amp;", "&");
            }
            System.out.println("cat: " + category);
            tagUrl = generalUrl + category;
            finalString = tagUrl + "-@-" + categoryID + "-@-" + category;
            tagUrlsAndIDs.add(finalString);
        }
        
        return tagUrlsAndIDs;
    }

}
