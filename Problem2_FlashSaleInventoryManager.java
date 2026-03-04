import java.util.*;

class InventoryManager {

    HashMap<String, Integer> stock = new HashMap<String, Integer>();
    HashMap<String, LinkedList<Integer>> waitingList = new HashMap<String, LinkedList<Integer>>();

    public InventoryManager() {
        stock.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedList<Integer>());
    }

    public int checkStock(String productId) {
        if (stock.containsKey(productId)) {
            return stock.get(productId);
        }
        return 0;
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.get(productId);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        } else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            int position = queue.size();
            return "Added to waiting list, position #" + position;
        }
    }
}

public class Problem2_FlashSaleInventoryManager {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB") + " units available");

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", 20000 + i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}