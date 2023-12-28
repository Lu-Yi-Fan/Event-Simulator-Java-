package cs2030.simulator;

import java.util.List;
import cs2030.util.ImList;

class Shop {
    private final ImList<Server> servers;

    public Shop(List<Server> servers) {
        this.servers = ImList.<Server>of(servers);
    }

    public Shop(ImList<Server> servers) {
        this.servers = servers;
    }
    
    public Shop() {
        this.servers = ImList.<Server>of();
    }
    
    public boolean containsSelfCheck() {
        boolean flag = false;
        for (Server s: this.servers) {
            if (s.checkSelfCheck()) {
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean atLeastOneSelfCheck() {
        ImList<Server> servers = this.getSelfCheckServers();
        for (Server s : servers) {
            if (s.getStatus() == "NotServing") {
                return true;
            }
        }
        return false;
    }
    
    public ImList<Server> getSelfCheckServers() {
        ImList<Server> servers = this.servers;
        ImList<Server> newLst = ImList.<Server>of();
        for (Server s : servers) {
            if (s.checkSelfCheck()) {
                newLst = newLst.add(s);
            }
        }
        return newLst;
    }
    
    public Shop add(Server server) {
        return new Shop(this.servers.add(server));
    }
    
    public Shop update(Server updateThis) {
        ImList<Customer> lstQ = ImList.<Customer>of();
        boolean isSelfCheckFlag = updateThis.checkSelfCheck();
        if (isSelfCheckFlag) {
            lstQ = updateThis.getLstQ();
        }
        ImList<Server> servers = this.servers;
        ImList<Server> newLst = ImList.<Server>of();
        for (Server server : servers) {
            if (updateThis.getID() == server.getID()) {
                newLst = newLst.add(updateThis);
            } else if (server.checkSelfCheck()) {
                server = server.updateServer(server.getID(),server.getStatus(),server.getMaxQ(),
                                             lstQ,server.getAvailTime(),server.getType());
                newLst = newLst.add(server);
            } else {
                newLst = newLst.add(server);
            }
        }
        return new Shop(newLst);
    }
    
    public boolean atLeastOneAvailableServer(double arriveTime) {
        int count = 0;
        for (Server server : this.servers) {
            
            if (server.getStatus() == "Resting") {
                if (arriveTime >= server.getAvailTime()) {
                    count++;
                }
            }
            if (server.getStatus() == "NotServing") {
                count++;
            }
            if (server.checkSelfCheck()) {
                if (arriveTime >= server.getAvailTime()) {
                    count++;
                }
            }
        }
        return (count >= 1);
    }
    
    public boolean canCustomerWait() {
        boolean flag = false;
        for (Server server : this.servers) {
            //check server in shop boolean canQueue()
            if (server.canQueue()) {
                flag = true;
            }
        }
        return flag;
    }

    public Server getAvailableServer(double arriveTime) {
        Server availServer = new Server(11);
        for (Server server : this.servers) {
            if (arriveTime >= server.getAvailTime()) {
                availServer = server;
                break;
            }
        }
        return  availServer;
    }
    
    public Server getBestSelfCheckServer(Server currentServer) {
        double availTime = currentServer.getAvailTime();
        Server newServer = currentServer;
        for (Server s : this.servers) {
            if (s.getType() == "SelfCheck" && s.getAvailTime() < availTime) {
                newServer = s;
            }
        }
        return newServer;
    }
    
    public Server getServerToQueue() {
        Server waitAtServer = new Server(33);
        for (Server server : this.servers) {
            if (server.getStatus() == "Serving" || server.getStatus() == "Resting") {
                if (server.canQueue()) {
                    waitAtServer = server;
                    break;
                }
            }
        }
        return waitAtServer;
    }
    
    public Server findServer(Server findServer) {
        for (Server server : this.servers) {
            if (server.getID() == findServer.getID()) {
                return server;
            }
        }
        return new Server(44);
    }
    
    @Override
    public String toString() {
        return this.servers.toString();
    }

}
