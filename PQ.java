package cs2030.util;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PQ<T> {
    private final PriorityQueue<T> queue;

    public PQ(Comparator <? super T> cmp) {
        this.queue = new PriorityQueue<T>(cmp);
    }
    
    public PQ(PriorityQueue<T> queue) {
        this.queue = new PriorityQueue<T>(queue);
    }

    public PQ<T> add(T item) {
        PQ<T> newPQ = new PQ<T>(this.queue);
        newPQ.queue.add(item);
        return newPQ;
    }

    public Pair<T, PQ<T>> poll() {

        PQ<T> newPQ = new PQ<T>(this.queue);
        T toRemove = newPQ.queue.poll();
        return Pair.of(toRemove, newPQ);
    }
    
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public int size() {
        return this.queue.size();
    }

    @Override 
    public String toString() {
        return this.queue.toString();
    }
}
