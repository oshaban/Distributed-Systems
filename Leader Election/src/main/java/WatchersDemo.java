import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class WatchersDemo implements Watcher {

    private ZooKeeper zooKeeper;
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String TARGET_ZNODE = "/target_znode";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        WatchersDemo watchersDemo = new WatchersDemo();
        watchersDemo.connectToZookeeper();
        watchersDemo.watchTargetZnode();
        watchersDemo.run();
        watchersDemo.close();
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
     *
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        synchronized (this.zooKeeper) {
            this.zooKeeper.wait();
        }
    }

    /**
     * Closes ZooKeeper
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        this.zooKeeper.close();
    }

    /**
     * Sets watchers on the target znode
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void watchTargetZnode() throws KeeperException, InterruptedException {

        Stat stat = this.zooKeeper.exists(TARGET_ZNODE, this);

        if (stat == null) {
            return;
        }

        byte[] data = zooKeeper.getData(TARGET_ZNODE, this, stat);
        List<String> children = zooKeeper.getChildren(TARGET_ZNODE, this);
        System.out.println("Data: " + new String(data) + " children: " + children);
    }

    /**
     * Handles ZooKeeper events
     *
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
                break;
            case NodeDeleted:
                System.out.println(TARGET_ZNODE + " was deleted");
                break;
            case NodeCreated:
                System.out.println(TARGET_ZNODE + " was created");
                break;
            case NodeDataChanged:
                System.out.println(TARGET_ZNODE + " data changed");
                break;
            case NodeChildrenChanged:
                System.out.println(TARGET_ZNODE + " children change");
                break;
        }

        try {
            // Gets all up to date data after changes and registers the watcher
            watchTargetZnode();
        } catch (KeeperException | InterruptedException e) {
        }
    }
}
