package nikos.steamcrawler;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBTools {

    static Connection con = null;
    static Statement stmt = null;

    private static void initDB() {
        if (con == null) {
            try {//kanw prwta to connection 
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gamesdb?useSSL=false&autoReconnect=true", "root", "");
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                //check = con.prepareStatement("SELECT movieName FROM ratingstable where movieName =?");
            } catch (SQLException ex) {
                System.out.println("ERROR: in initDB " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("ERROR: in initDB sto 2ro catch ");
                Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ArrayList<String> getGameUrlsFromDB() {
        initDB();
        ArrayList<String> gameUrls = new ArrayList<String>();
        String url = "";
        try {
            //System.out.println("select gameUrl from steamgameurlsandtitles");
            ResultSet rs = stmt.executeQuery("select gameUrl from steamgameurlsandtitles");
            while (rs.next()) {
                url = rs.getString("gameUrl");
                gameUrls.add(url);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: in getGameUrlsFromDB " + ex.getMessage());
            Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gameUrls;
    }
     public static ArrayList<String> getSteamGameTagUrlsAndIDs() {
        initDB();
        ArrayList<String> gamaUrlsAndIDs = new ArrayList<String>();
        try {
         
            ResultSet rs = stmt.executeQuery("select * from  steamgametagurls");
            String url = "";
            String id = "";
            while (rs.next()) {
                url = rs.getString("gameTagUrls");
                id = rs.getString("gameID");
                gamaUrlsAndIDs.add(url+"-@-"+id);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: in getTheContentsOfmovies2visit " + ex.getMessage());
            Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gamaUrlsAndIDs;
    }
    
    public static int tableSize(String table) {
        initDB();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("select count(*) from " + table);
            rs.next();
            return Integer.parseInt(rs.getString("count(*)"));
        } catch (SQLException ex) {
            System.out.println("ERROR: in tableIsEmpty " + ex.getMessage());
            Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static boolean tableIsEmpty(String table) {
        if (tableSize(table) == 0) {
            return true;
        }
        return false;
    }

    public static boolean deleteFromTable(String url, String field, String table) {
        initDB();
        try {
            stmt.executeUpdate("DELETE from " + table + " WHERE " + field + " = '" + url + "'");
        } catch (SQLException ex) {
            System.out.println("ERROR: in deleteFromTable " + ex.getMessage());
            Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

   

    public static void insertToTable(String val, String table) {
        initDB();
        if (!val.isEmpty()) {//an to movieurl dn einai keno ta vazw stin vasi
            try {
                System.out.println("INSERT into " + table + " VALUES (\"" + val + "\")");
                stmt.executeUpdate("INSERT into " + table + " VALUES (\"" + val + "\")");
            } catch (MySQLIntegrityConstraintViolationException ex) {
                System.out.println("already in db");
                return;
            } catch (SQLException ex) {
                System.out.println("ERROR: in insertToTable " + table + " " + ex.getMessage());
                Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

  

    public static ArrayList<String> getTheContentsOfTable(String field, String table) {
        initDB();
        ArrayList<String> movieUrls = new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery("select " + field + " from " + table);
            while (rs.next()) {
                movieUrls.add(rs.getString(field));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: in getTheContentsOfmovies2visit " + ex.getMessage());
            Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movieUrls;
    }

    public static ArrayList<String> unionTables(String table1, String table2, String field) {
        initDB();
        ArrayList<String> movieUrls = new ArrayList<String>();
        try {

            ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM " + table1 + " UNION " + "SELECT " + field + " FROM " + table2);
            while (rs.next()) {
                movieUrls.add(rs.getString(field));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: in getTheContentsOfmovies2visit " + ex.getMessage());
            Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("The union returned:" + movieUrls.size() + " unique records");
        return movieUrls;
    }

    
    public static void dumpMovieInfo() {
        initDB();
        try {
            ResultSet rs = stmt.executeQuery("select * from movieinfo");
            FileWriter writer = new FileWriter("movieinfo.txt");
            while (rs.next()) {
                String mUrl = rs.getString("movieUrl");
                /*String mName = rs.getString("movieName");
                String ageR = rs.getString("ageRating");
                String gerne = rs.getString("gerne");
                String dir = rs.getString("director");
                String dur = rs.getString("duration");
                String year = rs.getString("yearInTheaters");
                String imgUrl = rs.getString("imageUrl"); */
                writer.write(mUrl + "\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
