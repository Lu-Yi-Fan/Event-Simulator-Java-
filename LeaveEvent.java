package cs2030.simulator;


import cs2030.util.Lazy;
import cs2030.util.ImList;
import cs2030.util.Pair;
import java.util.Optional;
import java.util.function.Supplier;


class LeaveEvent extends EventStub {
    private final Lazy<Double> lazy;
    private final Supplier<Double> restTimes;
    
    public LeaveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
        this.lazy = Lazy.<Double>of(() -> 1.0);
        this.restTimes = () -> 0.0;
    }
    
    LeaveEvent(Customer customer, double eventTime,Lazy<Double> lazy) {
        super(customer, eventTime);
        this.lazy = lazy;
        this.restTimes = () -> 0.0;
    }
    
    LeaveEvent(Customer customer, double eventTime,Lazy<Double> lazy, Supplier<Double> restTimes) {
        super(customer, eventTime);
        this.lazy = lazy;
        this.restTimes = restTimes;
    }
    
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Optional<Event> optionalEvent = Optional.<Event>empty();
        return Pair.<Optional<Event>, Shop>of(optionalEvent, shop);
    }
    
    public String eventType() {
        return "LeaveEvent";
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
        return String.format("%s %s leaves", String.format("%.3f",super.getTime()),
                             super.getCust());
    }
}
