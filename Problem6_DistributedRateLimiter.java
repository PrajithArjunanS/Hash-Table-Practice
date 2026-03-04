import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    long lastRefillTime;

    public TokenBucket(int maxTokens) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        if (elapsed >= 3600000) {
            tokens = maxTokens;
            lastRefillTime = now;
        }
    }

    public boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        refill();
        return tokens;
    }

    public long getResetTime() {
        return lastRefillTime + 3600000;
    }
}

class RateLimiter {

    HashMap<String, TokenBucket> clients = new HashMap<String, TokenBucket>();
    int limit = 1000;

    public String checkRateLimit(String clientId) {

        if (!clients.containsKey(clientId)) {
            clients.put(clientId, new TokenBucket(limit));
        }

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            long retry = (bucket.getResetTime() - System.currentTimeMillis()) / 1000;
            return "Denied (0 requests remaining, retry after " + retry + "s)";
        }
    }

    public void getRateLimitStatus(String clientId) {

        if (!clients.containsKey(clientId)) {
            System.out.println("{used: 0, limit: " + limit + "}");
            return;
        }

        TokenBucket bucket = clients.get(clientId);
        int used = limit - bucket.getRemainingTokens();

        System.out.println("{used: " + used + ", limit: " + limit + ", reset: " + bucket.getResetTime() + "}");
    }
}

public class Problem6_DistributedRateLimiter {
    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        limiter.getRateLimitStatus("abc123");
    }
}