package cs2030.simulator;

import java.util.function.Supplier;
import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.Lazy;


class EventStub implements Event {
    private final Customer customer;
    private final double eventTime;

    EventStub(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
    }
    
    public String eventType() {
        return "";
    }
    
    public Server getServer() {
        return new Server(96);
    }
    
    public Lazy<Double> getLazy() {
        return Lazy.<Double>of(() -> 1.0);
    }
    
    public Supplier<Double> getRestTimes() {
        return () -> 96.0;
    }
    
    @Override
    public Customer getCust() {
        return this.customer;
    }
    
    @Override
    public double getTime() {
        return this.eventTime;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.<Optional<Event>,Shop>of(Optional.empty(),shop);
    }

    public String toString() {
        return String.format("%.3f",this.eventTime);
    }
}
