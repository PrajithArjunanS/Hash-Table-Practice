import java.util.*;

class VideoData {
    String videoId;
    String data;

    VideoData(String videoId, String data) {
        this.videoId = videoId;
        this.data = data;
    }
}

class MultiLevelCache {

    int L1_LIMIT = 10000;
    int L2_LIMIT = 100000;

    LinkedHashMap<String, VideoData> L1;
    LinkedHashMap<String, VideoData> L2;
    HashMap<String, VideoData> L3;

    int l1Hits = 0;
    int l2Hits = 0;
    int l3Hits = 0;

    MultiLevelCache() {

        L1 = new LinkedHashMap<String, VideoData>(L1_LIMIT, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_LIMIT;
            }
        };

        L2 = new LinkedHashMap<String, VideoData>(L2_LIMIT, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_LIMIT;
            }
        };

        L3 = new HashMap<String, VideoData>();

        for (int i = 1; i <= 20; i++) {
            String id = "video_" + i;
            L3.put(id, new VideoData(id, "VideoContent_" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        if (L2.containsKey(videoId)) {
            l2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData v = L2.get(videoId);
            L1.put(videoId, v);

            return v;
        }

        System.out.println("L2 Cache MISS");

        if (L3.containsKey(videoId)) {
            l3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            VideoData v = L3.get(videoId);
            L2.put(videoId, v);

            return v;
        }

        System.out.println("Video not found");
        return null;
    }

    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        double l1Rate = 0;
        double l2Rate = 0;
        double l3Rate = 0;

        if (total > 0) {
            l1Rate = (l1Hits * 100.0) / total;
            l2Rate = (l2Hits * 100.0) / total;
            l3Rate = (l3Hits * 100.0) / total;
        }

        System.out.println("L1: Hit Rate " + l1Rate + "%");
        System.out.println("L2: Hit Rate " + l2Rate + "%");
        System.out.println("L3: Hit Rate " + l3Rate + "%");
    }
}

public class Problem10_MultiLevelCacheSystem {
    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_5");
        cache.getVideo("video_5");
        cache.getVideo("video_12");
        cache.getVideo("video_5");

        cache.getStatistics();
    }
}