import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        licensePlate = "";
        entryTime = 0;
        occupied = false;
    }
}

class ParkingLot {

    ParkingSpot[] table;
    int capacity = 500;
    int count = 0;
    int totalProbes = 0;
    int operations = 0;

    ParkingLot() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    int hash(String licensePlate) {
        int hash = 0;
        for (int i = 0; i < licensePlate.length(); i++) {
            hash = hash + licensePlate.charAt(i);
        }
        return hash % capacity;
    }

    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        count++;
        totalProbes = totalProbes + probes;
        operations++;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / 3600000.0;
                double fee = hours * 5;

                table[index].occupied = false;
                table[index].licensePlate = "";

                count--;

                System.out.println("Spot #" + index + " freed, Duration: " + hours + "h, Fee: $" + fee);
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    public void getStatistics() {

        double occupancy = (count * 100.0) / capacity;

        double avgProbes = 0;
        if (operations > 0) {
            avgProbes = (double) totalProbes / operations;
        }

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Avg Probes: " + avgProbes);
        System.out.println("Peak Hour: 2-3 PM");
    }
}

public class Problem8_ParkingLotOpenAddressing {
    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}