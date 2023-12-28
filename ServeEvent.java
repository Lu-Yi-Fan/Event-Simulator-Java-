package cs2030.simulator;

import cs2030.util.Lazy;
import cs2030.util.ImList;
import cs2030.util.Pair;
import java.util.Optional;
import java.util.function.Supplier;


class ServeEvent extends EventStub {
    private final Server server;
    private final Lazy<Double> lazy;
    private final Supplier<Double> restTimes;

    public ServeEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = Lazy.<Double>of(() -> 1.0);
        this.restTimes = () -> 0.0;
    }
    
    ServeEvent(Customer customer, double eventTime, Server server, Lazy<Double> lazy) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = lazy;
        this.restTimes = () -> 0.0;
    }
    
    ServeEvent(Customer customer, double eventTime, Server server,
               Lazy<Double> lazy, Supplier<Double> restTimes) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = lazy;
        this.restTimes = restTimes;
    }
    
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        double arrivalTime = super.getTime();
        Customer cust = super.getCust();
        double defaultTime = 1.0;
        double serviceTime = this.lazy.get();
        if (serviceTime != 1.0) {
            defaultTime = serviceTime;
        }
        Server thisServer = this.server;
        thisServer = shop.findServer(thisServer);
        thisServer = thisServer.serverBusy(cust, defaultTime + arrivalTime,
                                           thisServer.getType());
        shop = shop.update(thisServer);
        Optional<Event> optionalEvent = Optional.<Event>of(new DoneEvent(cust,
                                                                         defaultTime + arrivalTime,
                                                                         thisServer,this.lazy,
                                                                         this.restTimes));
        return Pair.<Optional<Event>,Shop>of(optionalEvent,shop);
    }
    
    public String eventType() {
        return "ServeEvent";
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
        return String.format("%s %s serves by %s", String.format("%.3f",super.getTime()),
                             super.getCust(),this.server.toString());
    }
}

