import java.util.*;

class AutocompleteSystem {

    HashMap<String, Integer> queryFrequency = new HashMap<String, Integer>();

    public void addQuery(String query) {

        if (!queryFrequency.containsKey(query)) {
            queryFrequency.put(query, 0);
        }

        queryFrequency.put(query, queryFrequency.get(query) + 1);
    }

    public void updateFrequency(String query) {

        if (!queryFrequency.containsKey(query)) {
            queryFrequency.put(query, 1);
        } else {
            queryFrequency.put(query, queryFrequency.get(query) + 1);
        }

        System.out.println(query + " → Frequency: " + queryFrequency.get(query));
    }

    public void search(String prefix) {

        ArrayList<Map.Entry<String, Integer>> results = new ArrayList<Map.Entry<String, Integer>>();

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            String query = entry.getKey();

            if (query.startsWith(prefix)) {
                results.add(entry);
            }
        }

        Collections.sort(results, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });

        int count = 0;

        for (Map.Entry<String, Integer> entry : results) {

            if (count == 10) {
                break;
            }

            System.out.println((count + 1) + ". " + entry.getKey() + " (" + entry.getValue() + " searches)");
            count++;
        }
    }
}

public class Problem7_AutocompleteSystem {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java tutorial");
        system.addQuery("java 21 features");

        System.out.println("Search results for prefix 'jav':");
        system.search("jav");

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}