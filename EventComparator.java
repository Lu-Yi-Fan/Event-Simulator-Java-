package cs2030.simulator;

import java.util.Comparator;

class EventComparator implements Comparator<Event> {
    public int compare(Event thisEvent, Event otherEvent) {
        if (thisEvent.getTime() - otherEvent.getTime() > 0) {
            return 1;
        } else if (otherEvent.getTime() - thisEvent.getTime() > 0) {
            return -1;
        } else {
            if (thisEvent.getCust().getID() < otherEvent.getCust().getID()) {
                return -1;
            } else if (thisEvent.getCust().getID() > otherEvent.getCust().getID()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
