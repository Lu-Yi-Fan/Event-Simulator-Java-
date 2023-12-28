package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;

class Server {
    private final int id;
    private final String status; //"NotServing" "Serving" "Resting"
    private final int maxQ;
    private final ImList<Customer> lstQ;
    private final double nextAvailableTime;
    private final String type;
    
    Server(int id) {
        this.id = id;
        this.status = "NotServing";
        this.maxQ = 1;
        this.lstQ = ImList.<Customer>of();
        this.nextAvailableTime = 0;
        this.type = "Server";
    }
    
    Server(int id, int maxQ) {
        this.id = id;
        this.status = "NotServing";
        this.maxQ = maxQ;
        this.lstQ = ImList.<Customer>of();
        this.nextAvailableTime = 0;
        this.type = "Server";
    }
    
    Server(int id, int maxQ, String type) {
        this.id = id;
        this.status = "NotServing";
        this.maxQ = maxQ;
        this.lstQ = ImList.<Customer>of();
        this.nextAvailableTime = 0;
        this.type = type;
    }
    
    //Navigating events
    Server(int id, String status, int maxQ, ImList<Customer> lst, double time) {
        this.id = id;
        this.status = status;
        this.maxQ = maxQ;
        this.lstQ = lst;
        this.nextAvailableTime = time;
        this.type = "Server";
    }
    
    Server(int id, String status, int maxQ, ImList<Customer> lst, double time, String type) {
        this.id = id;
        this.status = status;
        this.maxQ = maxQ;
        this.lstQ = lst;
        this.nextAvailableTime = time;
        this.type = type;
    }

    public int getID() {
        return this.id;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public int getMaxQ() {
        return this.maxQ;
    }

    public ImList<Customer> getLstQ() {
        return this.lstQ;
    }
    
    public double getAvailTime() {
        return this.nextAvailableTime;
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean checkList(Customer customer) {
        boolean flag = false;
        for (Customer x : this.lstQ) {
            if (x.getID() == customer.getID()) {
                flag = true;
            }
        }
        return flag;
    }
    
    //can queue means can I wait at this server?
    public boolean canQueue() {
        //get the maxQ length
        //check the size of the queue
        return !(this.lstQ.size() == this.maxQ);
    }
    
    //check whether can add to waiting queue first (CANQUEUE METHOD)
    public Server addCustomerToWait(Customer customer) {
        ImList<Customer> lst = this.lstQ;
        lst = lst.add(customer);
        return new Server(this.id, this.status, this.maxQ, lst,
                          this.nextAvailableTime, this.type);
    }
    
    
    //server resting -> Resting
    public Server serverRest(double restTime) {
        //update status
        //update avail time
        return new Server(this.id, "Resting", this.maxQ,
                          this.lstQ, restTime);
    }
    
    //server serve customer -> Serving
    //remove fella from queue when serve
    public Server serverBusy(Customer customer, double serviceTime) {
        //check if in the waiting list, might be server first ever customer
        boolean flag = checkList(customer);
        ImList<Customer> lst = this.lstQ;
        if (flag) {
            int index = lst.indexOf(customer);
            lst = lst.remove(index).second();
        }
        return new Server(this.id, "Serving", this.maxQ, lst, serviceTime);
    }
    
    public Server serverBusy(Customer customer, double serviceTime, String type) {
        //check if in the waiting list, might be server first ever customer
        boolean flag = checkList(customer);
        ImList<Customer> lst = this.lstQ;
        if (flag) {
            int index = lst.indexOf(customer);
            lst = lst.remove(index).second();
        }
        return new Server(this.id, "Serving", this.maxQ, lst,
                          serviceTime, type);
    }
    
    //server done serving
    //STATUS -> NotServing
    public Server doneServing() {
        return new Server(this.id, "NotServing", this.maxQ,
                          this.lstQ, this.nextAvailableTime);
    }
    
    public Server doneServing(String type) {
        return new Server(this.id, "NotServing", this.maxQ,
                          this.lstQ, this.nextAvailableTime, type);
    }
    
    public boolean checkSelfCheck() {
        //true if is SelfCheck
        return this.type == "SelfCheck";
    }
    
    public Server updateServer(int id, String status, int maxQ,
                               ImList<Customer> lst, double time, String type) {
        return new Server(id, status, maxQ,lst,time,type);
    }

    public String toString() {
        if (this.checkSelfCheck()) {
            return String.format("self-check %d",this.id);
        }
        return String.format("%d",this.id);
    }
}
