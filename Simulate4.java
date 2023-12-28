package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;

import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;

public class Simulate4 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate4(int numOfServer, List<Double> lstArrivalTime) {
        this.pq = makePq(ImList.<Double>of(lstArrivalTime));
        this.shop = makeShop(numOfServer);
    }

    public static PQ<Event> makePq(ImList<Double> lstArrivalTime) {
        Comparator<Event> cmp = new EventComparator();
        PQ<Event> pq = new PQ<Event>(cmp);
        for (int i = 1; i < lstArrivalTime.size() + 1; i++) {
            //create customer
            Customer customer = new Customer(i, lstArrivalTime.get(i - 1));
            //create event time
            EventStub eventstub = new ArriveEvent(customer, lstArrivalTime.get(i - 1));
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
        //track customer not served(Leave Event)
        //track customer served (Serve event)
        //track waiting time(Serve event)
        String result = "";
        PQ<Event> newPq = this.pq;
        Shop shop = this.shop;
        ImList<Event> lst = ImList.<Event>of();
        while (newPq.isEmpty() == false) {
            Event event = newPq.poll().first();
            newPq = newPq.poll().second();
            //execute event
            Pair<Optional<Event>, Shop> executeEvent = event.execute(shop);
            //assign the event and newShop
            Optional<Event> optionalEvent = executeEvent.first();
            shop = executeEvent.second();
            if (executeEvent.first() != Optional.<Event>empty()) {
                lst = lst.add(optionalEvent.orElseThrow());
                newPq = newPq.add(optionalEvent.orElseThrow());
            }
            result += event.toString();
            result += "\n";
        }
        Statistic tracker = new Statistic(lst);
        result += tracker.toString();
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s",this.pq,this.shop);
    }
}
