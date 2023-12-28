package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import cs2030.util.Lazy;
import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;

public class Simulate5 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate5(int numOfServer, List<Pair<Double, Supplier<Double>>> lst) {
        this.pq = makePq(ImList.<Pair<Double, Supplier<Double>>>of(lst));
        this.shop = makeShop(numOfServer);
    }
    
    public static PQ<Event> makePq(ImList<Pair<Double, Supplier<Double>>> lst) {
        Comparator<Event> cmp = new EventComparator();
        PQ<Event> pq = new PQ<Event>(cmp);
        int count = 1;
        for (Pair<Double, Supplier<Double>> pair : lst) {
            Customer customer = new Customer(count, pair.first());
            Lazy<Double> lazy =  new Lazy<Double>(pair.second());
            EventStub eventstub = new ArriveEvent(customer,pair.first(),lazy);
            pq = pq.add(eventstub);
            count++;
        }
        return pq;
        
    }

    public static Shop makeShop(int numOfServer) {
        Shop shop = new Shop();
        for (int i = 1; i < numOfServer + 1; i++) {
            //create server
            //add to list
            shop = shop.add(new Server(i));
        }
        return shop;
    }
    
    public String run() {
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

