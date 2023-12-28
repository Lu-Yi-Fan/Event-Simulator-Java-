package cs2030.simulator;

import java.util.Comparator;
import java.util.List;

import cs2030.util.ImList;
import cs2030.util.PQ;

public class Simulate2 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate2(int numOfServer, List<Double> lstArrivalTime) {
        this.pq = makePq(ImList.<Double>of(lstArrivalTime));
        this.shop = makeShop(numOfServer);
    }

    public static PQ<Event> makePq(ImList<Double> lstArrivalTime) {
        Comparator<Event> cmp = new EventComparator();
        PQ<Event> pq = new PQ<Event>(cmp);
        for (int i = 1; i < lstArrivalTime.size() + 1; i++) {
            Customer customer = new Customer(i, lstArrivalTime.get(i - 1));
            EventStub eventstub = new EventStub(customer, lstArrivalTime.get(i - 1));
            pq = pq.add(eventstub);
        }
        return pq; 
    }

    public static Shop makeShop(int numOfServer) {
        Shop shop = new Shop(); 
        for (int i = 1; i < numOfServer + 1; i++) {
            shop = shop.add(new Server(i));
        }
        return shop;
    }
    
    public PQ<Event> getPq() {
        return this.pq;
    }

    public Shop getShop() {
        return this.shop;
    }

    public String run() {
        String result = "";
        PQ<Event> newPq = this.pq;
        int num = newPq.size();
        for (int i = 0; i < num; i++) {
            result += newPq.poll().first();
            result += "\n";
            newPq = newPq.poll().second();
        }
        result += "-- End of Simulation --";
        return result;
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s",this.pq,this.shop);
    }
}
