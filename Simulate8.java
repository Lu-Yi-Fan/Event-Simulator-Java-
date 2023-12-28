package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import cs2030.util.Lazy;
import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;

public class Simulate8 {
    private final PQ<Event> pq;
    private final Shop shop;
    private final Supplier<Double> restTimes;
    private final ImList<Customer> sharedQueue;

    public Simulate8(int numOfServer, int numOfSelfCheckOut, List<Pair<Double,
                      Supplier<Double>>> lst, int qMax, Supplier<Double> restTimes) {
        this.pq = makePq(ImList.<Pair<Double, Supplier<Double>>>of(lst),restTimes);
        this.shop = makeShop(numOfServer,numOfSelfCheckOut, qMax);
        this.restTimes = restTimes;
        this.sharedQueue = ImList.<Customer>of();
    }
    
    public static PQ<Event> makePq(ImList<Pair<Double, Supplier<Double>>> lst,
                                   Supplier<Double> restTimes) {
        Comparator<Event> cmp = new EventComparator();
        PQ<Event> pq = new PQ<Event>(cmp);
        int count = 1;
        for (Pair<Double, Supplier<Double>> pair : lst) {
            Customer customer = new Customer(count, pair.first());
            Lazy<Double> lazy =  new Lazy<Double>(pair.second());
            EventStub eventstub = new ArriveEvent(customer,pair.first(),lazy,restTimes);
            pq = pq.add(eventstub);
            count++;
        }
        return pq;
        
    }
    
    public static Shop makeShop(int numOfServer, int numOfSelfCheckOut, int qMax) {
        Shop shop = new Shop();
        for (int i = 1; i < numOfServer + 1; i++) {
            shop = shop.add(new Server(i,qMax,"Server"));
        }
        for (int i = numOfServer + 1; i <  numOfServer + numOfSelfCheckOut + 1; i++) {
            shop = shop.add(new Server(i,qMax,"SelfCheck"));
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
            boolean dontAdd = true;
            boolean selfCheckOutFlag = false;
            Server thisServer = event.getServer();
            Server findServer = shop.findServer(thisServer);
            Customer getCustomer = event.getCust();
            if (event.eventType() == "ServeEvent" &&
                findServer.getStatus() == "Serving") {
                //serve event and the customer is in waiting queue
                if (findServer.checkSelfCheck()) {
                    findServer = shop.getBestSelfCheckServer(findServer);
                }
                double newTime = findServer.getAvailTime();
                event = new ServeEvent(event.getCust(), newTime, findServer,
                                       event.getLazy(), event.getRestTimes());
                dontAdd = false;
                ImList<Event> newLst = ImList.<Event>of();
                for  (Event e : lst) {
                    if (e.eventType() == "ServeEvent" &&
                        e.getCust().getID() == getCustomer.getID()) {
                        newLst = newLst.add(event);
                    } else {
                        newLst = newLst.add(e);
                    }
                }
                lst = newLst;
                newPq = newPq.add(event);
            } else if (event.eventType() == "ServeEvent" &&
                       findServer.getStatus() == "Resting") {
                if (event.getTime() >= findServer.getAvailTime()) {
                    Pair<Optional<Event>, Shop> executeEvent = event.execute(shop);
                    Optional<Event> optionalEvent = executeEvent.first();
                    shop = executeEvent.second();
                    if (executeEvent.first() != Optional.<Event>empty()) {
                        lst = lst.add(optionalEvent.orElseThrow());
                        newPq = newPq.add(optionalEvent.orElseThrow());
                    }
                } else {
                    event = new ServeEvent(event.getCust(), findServer.getAvailTime(),
                                           findServer, event.getLazy(), event.getRestTimes());
                    dontAdd = false;
                    ImList<Event> newLst = ImList.<Event>of();
                    for  (Event e : lst) {
                        if (e.eventType() == "ServeEvent" &&
                            e.getCust().getID() == getCustomer.getID()) {
                            newLst = newLst.add(event);
                        } else {
                            newLst = newLst.add(e);
                        }
                    }
                    lst = newLst;
                    newPq = newPq.add(event);
                }
            } else {
                Pair<Optional<Event>, Shop> executeEvent = event.execute(shop);
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


