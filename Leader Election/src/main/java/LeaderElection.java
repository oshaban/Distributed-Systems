import org.apache.zookeeper.*;

import java.io.IOException;

public class LeaderElection implements Watcher {

    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private ZooKeeper zooKeeper;
    private static final int SESSION_TIMEOUT = 3000;

    public static void main(String[] args) throws IOException, InterruptedException {
        LeaderElection leaderElection = new LeaderElection();
        leaderElection.connectToZookeeper();
        leaderElection.run();
        leaderElection.close();
        System.out.println("Disconnected from Zookeeper, exiting application");
    }

    /**
     * Connects to ZooKeeper client
     *
     * @throws IOException
     */
    public void connectToZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    /**
     * Puts the main thread into a wait-state
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        synchronized (this.zooKeeper) {
            this.zooKeeper.wait();
        }
    }

    /**
     * Closes ZooKeeper
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        this.zooKeeper.close();
    }

    /**
     * Handles ZooKeeper events
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Successfully connected to Zookeeper");
                } else {
                    // Wakes up the main thread and closes resources
                    synchronized (this.zooKeeper) {
                        System.out.println("Disconnected from Zookeeper event");
                        zooKeeper.notifyAll();
                    }
                }
        }
    }
}
