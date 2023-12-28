package cs2030.simulator;

class Customer {
    private final int id;
    private final double arrivalTime;

    Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public int getID() {
        return this.id;
    }
    
    public double getArrivalTime() {
        return this.arrivalTime;
    }
    
    @Override
    public String toString() {
        return String.format("%d",this.id);
    }
}
