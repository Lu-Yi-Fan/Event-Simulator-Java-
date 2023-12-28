package cs2030.simulator;

import cs2030.util.ImList;

class Statistic {
    private final ImList<Event> events;

    Statistic(ImList<Event> events) {
        this.events = events;
    }
    
    int numServed() {
        int count = 0;
        for (Event event : this.events) {
            if (event.eventType() == "ServeEvent") {
                count ++;
            }
        }
        return count;
    }
    
    int numLeft() {
        int count = 0;
        for (Event event : this.events) {
            if (event.eventType() == "LeaveEvent") {
                count ++;
            }
        }
        return count;
    }
    
    double time() {
        int numServed = this.numServed();
        double total = 0.0;
        for (Event event : this.events) {
            if (event.eventType() == "ServeEvent") {
                double arrivalTime = event.getCust().getArrivalTime(); //get customer then get arrival time
                double serveTime = event.getTime(); // event time
                total += serveTime - arrivalTime;
            }
        }
        return total / numServed;
    }

    @Override
    public String toString() {
        return String.format("[%.3f %d %d]",this.time(), this.numServed(), this.numLeft());
    }
}
