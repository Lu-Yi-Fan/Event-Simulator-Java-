package cs2030.simulator;

import cs2030.util.Lazy;
import cs2030.util.ImList;
import cs2030.util.Pair;
import java.util.Optional;
import java.util.function.Supplier;

class WaitEvent extends EventStub {
    private final Server server;
    private final Lazy<Double> lazy;
    private final Supplier<Double> restTimes;

    public WaitEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = Lazy.<Double>of(() -> 1.0);
        this.restTimes = () -> 0.0;
    }
    
    WaitEvent(Customer customer, double eventTime, Server server,Lazy<Double> lazy) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = lazy;
        this.restTimes = () -> 0.0;
    }
    
    WaitEvent(Customer customer, double eventTime, Server server, Lazy<Double> lazy,
              Supplier<Double> restTimes) {
        super(customer, eventTime);
        this.server = server;
        this.lazy = lazy;
        this.restTimes = restTimes;
    }

    // Take in customer and serve -> Update Shop update Server
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        double arrivalTime = super.getTime();
        Customer cust = super.getCust();
        Server thisServer = this.server;
        thisServer = shop.findServer(thisServer);
        Server updateServer = thisServer.addCustomerToWait(cust);
        shop = shop.update(updateServer);
        
        Optional<Event> optionalEvent = Optional.<Event>of(new
                                                           ServeEvent(
                                                                      cust,
                                                                      updateServer.getAvailTime(),
                                                                      updateServer,
                                                                      this.lazy,
                                                                      this.restTimes));
        if (updateServer.checkSelfCheck()) {
            updateServer = shop.getBestSelfCheckServer(updateServer);
            optionalEvent = Optional.<Event>of(new ServeEvent(cust,
                                                              updateServer.getAvailTime(),
                                                              updateServer,
                                                              this.lazy,
                                                              this.restTimes));
        }
        return Pair.<Optional<Event>,Shop>of(optionalEvent, shop);
    }
    
    public String eventType() {
        return "WaitEvent";
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
        return String.format("%s %s waits at %s", String.format("%.3f",super.getTime()),
                             super.getCust(),this.server.toString());
    }
}


