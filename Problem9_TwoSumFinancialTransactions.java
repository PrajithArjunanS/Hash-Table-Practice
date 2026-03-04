import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    int time;

    Transaction(int id, int amount, String merchant, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
    }
}

class TransactionAnalyzer {

    ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<Integer, Transaction>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction t2 = map.get(complement);
                System.out.println("Pair: (" + t2.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    public void findTwoSumWithTimeWindow(int target, int window) {

        HashMap<Integer, Transaction> map = new HashMap<Integer, Transaction>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction t2 = map.get(complement);

                if (Math.abs(t.time - t2.time) <= window) {
                    System.out.println("Time Window Pair: (" + t2.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    public void detectDuplicates() {

        HashMap<String, ArrayList<Transaction>> map = new HashMap<String, ArrayList<Transaction>>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<Transaction>());
            }

            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            ArrayList<Transaction> list = map.get(key);

            if (list.size() > 1) {
                System.out.print("Duplicate: ");
                for (Transaction t : list) {
                    System.out.print(t.id + " ");
                }
                System.out.println();
            }
        }
    }

    public void findKSum(int k, int target) {
        ArrayList<Integer> current = new ArrayList<Integer>();
        findKSumHelper(0, k, target, current);
    }

    public void findKSumHelper(int start, int k, int target, ArrayList<Integer> current) {

        if (k == 0 && target == 0) {
            System.out.println(current);
            return;
        }

        if (k == 0 || target < 0) {
            return;
        }

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            current.add(t.id);

            findKSumHelper(i + 1, k - 1, target - t.amount, current);

            current.remove(current.size() - 1);
        }
    }
}

public class Problem9_TwoSumFinancialTransactions {
    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", 600));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", 615));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", 630));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", 640));

        analyzer.findTwoSum(500);

        analyzer.findTwoSumWithTimeWindow(500, 60);

        analyzer.detectDuplicates();

        analyzer.findKSum(3, 1000);
    }
}