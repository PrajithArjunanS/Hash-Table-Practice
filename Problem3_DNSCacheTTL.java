import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttl) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttl * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    HashMap<String, DNSEntry> cache = new HashMap<String, DNSEntry>();
    LinkedList<String> usageOrder = new LinkedList<String>();

    int capacity = 5;

    int hits = 0;
    int misses = 0;

    public String resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                usageOrder.remove(domain);
                usageOrder.addLast(domain);
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain);
                usageOrder.remove(domain);
            }
        }

        misses++;

        String ip = queryUpstreamDNS(domain);

        if (cache.size() >= capacity) {
            String lru = usageOrder.removeFirst();
            cache.remove(lru);
        }

        DNSEntry newEntry = new DNSEntry(domain, ip, 5);
        cache.put(domain, newEntry);
        usageOrder.addLast(domain);

        return "Cache MISS → " + ip;
    }

    String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + (200 + r.nextInt(50));
    }

    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = 0;

        if (total > 0) {
            hitRate = (hits * 100.0) / total;
        }

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

public class Problem3_DNSCacheTTL {
    public static void main(String[] args) throws Exception {

        DNSCache dns = new DNSCache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}