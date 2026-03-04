import java.util.*;

class PlagiarismDetector {

    HashMap<String, HashSet<String>> index = new HashMap<String, HashSet<String>>();
    HashMap<String, ArrayList<String>> documents = new HashMap<String, ArrayList<String>>();
    int n = 5;

    public void addDocument(String docId, String text) {

        String[] words = text.toLowerCase().split(" ");
        ArrayList<String> ngrams = new ArrayList<String>();

        for (int i = 0; i <= words.length - n; i++) {
            String gram = "";

            for (int j = i; j < i + n; j++) {
                gram = gram + words[j] + " ";
            }

            gram = gram.trim();
            ngrams.add(gram);

            if (!index.containsKey(gram)) {
                index.put(gram, new HashSet<String>());
            }

            index.get(gram).add(docId);
        }

        documents.put(docId, ngrams);
    }

    public void analyzeDocument(String docId) {

        ArrayList<String> grams = documents.get(docId);
        HashMap<String, Integer> matchCount = new HashMap<String, Integer>();

        for (String gram : grams) {

            if (index.containsKey(gram)) {

                HashSet<String> docs = index.get(gram);

                for (String d : docs) {

                    if (!d.equals(docId)) {

                        if (!matchCount.containsKey(d)) {
                            matchCount.put(d, 0);
                        }

                        matchCount.put(d, matchCount.get(d) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + grams.size() + " n-grams");

        for (String d : matchCount.keySet()) {

            int matches = matchCount.get(d);
            double similarity = (matches * 100.0) / grams.size();

            if (similarity > 50) {
                System.out.println("Found " + matches + " matching n-grams with \"" + d + "\"");
                System.out.println("Similarity: " + similarity + "% (PLAGIARISM DETECTED)");
            } else {
                System.out.println("Found " + matches + " matching n-grams with \"" + d + "\"");
                System.out.println("Similarity: " + similarity + "% (suspicious)");
            }
        }
    }
}

public class Problem4_PlagiarismDetectionSystem {

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "data science is an interdisciplinary field that uses scientific methods and algorithms";
        String essay2 = "data science is an interdisciplinary field that uses scientific methods for analysis";
        String essay3 = "machine learning and artificial intelligence are important parts of modern technology";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay1);

        detector.analyzeDocument("essay_123.txt");
    }
}