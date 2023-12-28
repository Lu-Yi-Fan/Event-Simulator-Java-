package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import cs2030.util.Lazy;
import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;

public class Simulate6 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate6(int numOfServer, List<Pair<Double, Supplier<Double>>> lst,int qMax) {
        this.pq = makePq(ImList.<Pair<Double, Supplier<Double>>>of(lst));
        this.shop = makeShop(numOfServer, qMax);
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
    
    public static Shop makeShop(int numOfServer,int qMax) {
        Shop shop = new Shop();
        for (int i = 1; i < numOfServer + 1; i++) {
            //create server
            //add to list
            shop = shop.add(new Server(i,qMax));
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
            //check event -> if is serve event see if server busy
            //eg waiting got 2 people and they get poll and execute
            //but shouldnt be so i make new serve!
            //get shop //find server //is server free//
            boolean dontAdd = true;
            Server thisServer = event.getServer();
            Server findServer = shop.findServer(thisServer);
            //get server from shop because shop updates but server never
            Customer getCustomer = event.getCust();
            if (event.eventType() == "ServeEvent" &&
                findServer.getStatus() != "NotServing") {
                //serve event and the customer is in waiting queue
                //create new serve event -> got new time already
                //serve event details -> customer, new time, server and lazy
                //new time based off the server
                double newTime = findServer.getAvailTime();
                event = new ServeEvent(event.getCust(), newTime, findServer, event.getLazy());
                dontAdd = false;
                //cannot execute so i manually add to pq
                //need to update serve event in lst cos new serve event ->
                //find the customer and update serve event!!!
                //iterate event through lst and find event where event =
                //serve event and customer is the same then i update
                ImList<Event> newLst = ImList.<Event>of();
                for  (Event e : lst) {
                    if (e.eventType() == "ServeEvent" && e.getCust().getID()
                        == getCustomer.getID()) {
                        newLst = newLst.add(event);
                    } else {
                        newLst = newLst.add(e);
                    }
                }
                lst = newLst;
                newPq = newPq.add(event);
            //need to update the moment person is served
            } else {
                //execute event
                Pair<Optional<Event>, Shop> executeEvent = event.execute(shop);
                //assign the event and newShop
                Optional<Event> optionalEvent = executeEvent.first();
                shop = executeEvent.second();
                if (executeEvent.first() != Optional.<Event>empty()) {
                    lst = lst.add(optionalEvent.orElseThrow());
                    newPq = newPq.add(optionalEvent.orElseThrow());
                }
            }
            if (dontAdd) {
                result += event.toString();
                result += "\n";
            }
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


