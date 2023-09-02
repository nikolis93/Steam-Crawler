package nikos.steamcrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Connect {
    static long lastCon = 0;
    static int period = 5000; //60k= 1 min
    public static Document connect(String url) {
        long elapsed = System.currentTimeMillis() - lastCon;
        if (elapsed < period) {
            try {
                System.out.println("sleeping...");
                Thread.sleep(period - elapsed);
            } catch (Exception e) {
            }
        }
        lastCon = System.currentTimeMillis();
        try {
            return Jsoup.connect(url).timeout(30 * 1000).get(); //.timeout(30 * 1000).get()
        } catch (Exception e) {
            System.out.println("ERROR: FTEEI TO CONNECTION " + e.getMessage());
            return null;
        }
    }
}
