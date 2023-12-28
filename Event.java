package cs2030.simulator;

import java.util.function.Supplier;
import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.Lazy;


interface Event {
    public Customer getCust();
    
    public double getTime();
    
    public String eventType();
    
    public Server getServer();
    
    public Lazy<Double> getLazy();
    
    public Supplier<Double> getRestTimes();
    
    Pair<Optional<Event>, Shop> execute(Shop shop);
}
