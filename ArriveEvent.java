package cs2030.simulator;

import cs2030.util.Lazy;
import cs2030.util.ImList;
import cs2030.util.Pair;
import java.util.Optional;
import java.util.function.Supplier;


class ArriveEvent extends EventStub {
    private final Lazy<Double> lazy;
    private final Supplier<Double> restTimes;
    
    ArriveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
        this.lazy = Lazy.<Double>of(() -> 1.0);
        this.restTimes = () -> 0.0;
    }
    
    public ArriveEvent(Customer customer, double eventTime, Lazy<Double> lazy) {
        super(customer, eventTime);
        this.lazy = lazy;
        this.restTimes = () -> 0.0;
    }
    
    public ArriveEvent(Customer customer, double eventTime,
                       Lazy<Double> lazy, Supplier<Double> restTimes) {
        super(customer, eventTime);
        this.lazy = lazy;
        this.restTimes = restTimes;
    }
    
    /*
    ARRIVE -> SERVE -> DONE
    ARRIVE -> WAIT -> SERVE -> DONE
    ARRIVE -> LEAVE
     */
    
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        double arrivalTime = super.getTime();
        Customer cust = super.getCust();
        Optional<Event> optionalEvent = Optional.<Event>of(new LeaveEvent(cust, arrivalTime));
        //Scenario 1: Serve -> ALL SERVER IN SHOP FREE -> RETURN SERVE EVENT
        //now that i have resting servers i need to check for available servers using available time
        //arrival event => arrive time
        if (shop.atLeastOneAvailableServer(arrivalTime)) {
            Server server = shop.getAvailableServer(arrivalTime);
            //get serve event timing
            optionalEvent = Optional.<Event>of(new ServeEvent(cust, arrivalTime,
                                                              server, this.lazy, this.restTimes));
        } else if (shop.atLeastOneAvailableServer(arrivalTime) == false
                  && shop.canCustomerWait() == true) {
            //Scenario 2: Wait
            Server newServer = shop.getServerToQueue();
            optionalEvent = Optional.<Event>of(new WaitEvent(cust, arrivalTime,
                                                             newServer, this.lazy, this.restTimes));
        } else {
            //Scenario 3: Leave
            optionalEvent = Optional.<Event>of(new LeaveEvent(cust,
                                                              arrivalTime));
        }

        return Pair.<Optional<Event>,Shop>of(optionalEvent,shop);
    }
 
    public String eventType() {
        return "ArriveEvent";
    }
    
    public double getTime() {
        return super.getTime();
    }
    
    public Customer getCust() {
        return super.getCust();
    }
    
    public Server getServer() {
        //no server for this event so return dummy server but wont be called
        return new Server(96);
    }
    
    public Lazy<Double> getLazy() {
        return this.lazy;
    }
    
    public Supplier<Double> getRestTimes() {
        return this.restTimes;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s arrives", String.format("%.3f", super.getTime()),
                super.getCust()); 
    }
}
