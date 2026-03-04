import java.util.*;

class AnalyticsSystem {

    HashMap<String, Integer> pageViews = new HashMap<String, Integer>();
    HashMap<String, HashSet<String>> uniqueVisitors = new HashMap<String, HashSet<String>>();
    HashMap<String, Integer> trafficSources = new HashMap<String, Integer>();

    public void processEvent(String url, String userId, String source) {

        if (!pageViews.containsKey(url)) {
            pageViews.put(url, 0);
        }
        pageViews.put(url, pageViews.get(url) + 1);

        if (!uniqueVisitors.containsKey(url)) {
            uniqueVisitors.put(url, new HashSet<String>());
        }
        uniqueVisitors.get(url).add(userId);

        if (!trafficSources.containsKey(source)) {
            trafficSources.put(source, 0);
        }
        trafficSources.put(source, trafficSources.get(source) + 1);
    }

    public void getDashboard() {

        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(pageViews.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });

        System.out.println("Top Pages:");

        int count = 0;

        for (Map.Entry<String, Integer> entry : list) {

            if (count == 10) {
                break;
            }

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((count + 1) + ". " + url + " - " + views + " views (" + unique + " unique)");

            count++;
        }

        int total = 0;

        for (int v : trafficSources.values()) {
            total = total + v;
        }

        System.out.println();
        System.out.println("Traffic Sources:");

        for (String src : trafficSources.keySet()) {

            int c = trafficSources.get(src);
            double percent = (c * 100.0) / total;

            System.out.println(src + ": " + percent + "%");
        }
    }
}

public class Problem5_WebsiteAnalyticsDashboard {
    public static void main(String[] args) {

        AnalyticsSystem system = new AnalyticsSystem();

        system.processEvent("/article/breaking-news", "user_123", "google");
        system.processEvent("/article/breaking-news", "user_456", "facebook");
        system.processEvent("/sports/championship", "user_111", "google");
        system.processEvent("/sports/championship", "user_222", "direct");
        system.processEvent("/sports/championship", "user_333", "google");
        system.processEvent("/article/breaking-news", "user_123", "google");

        system.getDashboard();
    }
}