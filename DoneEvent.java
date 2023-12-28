package cs2030.simulator;

import cs2030.util.Lazy;
import cs2030.util.ImList;
import cs2030.util.Pair;
import java.util.Optional;
import java.util.function.Supplier;

class DoneEvent extends EventStub {
    private final Server server;
    private final Lazy<Double> lazy;
    private final Supplier<Double> restTimes;
    
    DoneEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = Lazy.<Double>of(() -> 1.0);
        this.restTimes = () -> 0.0;
    }
    
    public DoneEvent(Customer customer, double eventTime, Server server, Lazy<Double> lazy) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = lazy;
        this.restTimes = () -> 0.0;
    }
    
    public DoneEvent(Customer customer, double eventTime, Server server,
                     Lazy<Double> lazy, Supplier<Double> restTimes) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = lazy;
        this.restTimes = restTimes;
    }
    
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        double arrivalTime = super.getTime();
        Customer cust = super.getCust();
        //server rest
        //get rest
        //server change status and time
        Server thisServer = this.server;
        thisServer = shop.findServer(thisServer);
        if (thisServer.checkSelfCheck()) {
            //this is self check our
            //no rest
            //change to done
            thisServer = thisServer.doneServing(thisServer.getType());
        } else {
            double breakTime = this.restTimes.get();
            if (breakTime != 0.0) {
                thisServer = thisServer.serverRest(breakTime + thisServer.getAvailTime());
            } else {
                thisServer = thisServer.doneServing();
            }
        }
        shop = shop.update(thisServer);
        Optional<Event> optionalEvent = Optional.<Event>empty();
        return Pair.<Optional<Event>,Shop>of(optionalEvent,shop);
    }
    
    public String eventType() {
        return "DoneEvent";
    }
    
    public double getTime() {
        return super.getTime();
    }
    
    public Customer getCust() {
        return super.getCust();
    }
    
    public Server getServer() {
        return this.server;
    }
    
    public Lazy<Double> getLazy() {
        return this.lazy;
    }
    
    public Supplier<Double> getRestTimes() {
        return this.restTimes;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s done serving by %s", String.format("%.3f",super.getTime()),
                             super.getCust(),this.server.toString());
    }
}
