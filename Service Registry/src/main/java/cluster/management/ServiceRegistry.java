package cluster.management;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This will register a node/server with a Service Registry that is implemented in ZooKeeper
 */
public class ServiceRegistry implements Watcher {

    private static final String REGISTRY_ZNODE = "/service_registry";
    private final ZooKeeper zooKeeper;
    private String currentZnode = null;
    private List<String> allServiceAddresses = null; // Stores cached addresses in the Service Registry

    public ServiceRegistry(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        this.zooKeeper = zooKeeper;
        createServiceRegistryZnode();
    }

    /**
     * Registers a server/node to the service registry
     * <p>
     * This will create an ephemeral znode for the server that is registering itself
     *
     * @param metadata
     */
    public void registerToCluster(String metadata) throws KeeperException, InterruptedException {
        this.currentZnode = zooKeeper.create(REGISTRY_ZNODE + "/n_", metadata.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Registered to service registry");
    }

    /**
     * Registers the current server/node for updates when it first connects to ZooKeeper
     */
    public void registerForUpdates() {
        try {
            updateAddresses();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of service addresses in the cluster
     *
     * @return List of service addresses
     */
    public synchronized List<String> getAllServiceAddresses() throws KeeperException, InterruptedException {
        if (this.allServiceAddresses == null) {
            // Addresses are not registered yet, so update them
            updateAddresses();
        }
        return this.allServiceAddresses;
    }

    /**
     * Unregisters the current server/node from the cluster
     */
    public void unregisterFromCluster() throws KeeperException, InterruptedException {
        // Check if the node is registered with ZooKeeper
        if (currentZnode != null && this.zooKeeper.exists(currentZnode, false) != null) {
            this.zooKeeper.delete(currentZnode, -1);
        }
    }

    /**
     * Creates Service Registry znode if it doesn't already exist
     */
    private void createServiceRegistryZnode() throws KeeperException, InterruptedException {
        try {
            if (this.zooKeeper.exists(REGISTRY_ZNODE, false) == null) {
                // Node does not exist
                zooKeeper.create(REGISTRY_ZNODE, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            // Catches race condition between checking if the node exists, and creating the node
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the list of addresses for all servers/nodes registered with the service discovery
     * <p>
     * This operation happens atomically to avoid the current server/node updating the addresses at the same time
     */
    private synchronized void updateAddresses() throws KeeperException, InterruptedException {
        // Gets all children/znodes of the registry znode and sets up a watch
        // A watch is setup so if children in the zookeeper cluster change, Ex. a server joined or left the cluster, the current server will be alerted
        List<String> workerZnodes = this.zooKeeper.getChildren(REGISTRY_ZNODE, this);

        // Stores addresses in the cluster
        List<String> addresses = new ArrayList<>(workerZnodes.size());

        for (String workerZnode : workerZnodes) {
            String workerZnodeFullPath = REGISTRY_ZNODE + "/" + workerZnode;
            Stat stat = this.zooKeeper.exists(workerZnodeFullPath, false);

            // Check if node still exists -> race condition between when we fetch the node, and when we use the node
            if (stat == null) {
                continue;
            }
            byte[] addressBytes = this.zooKeeper.getData(workerZnodeFullPath, false, stat);
            String address = new String(addressBytes);
            addresses.add(address);
        }
        this.allServiceAddresses = Collections.unmodifiableList(addresses);
        System.out.println("The cluster addresses are: " + this.allServiceAddresses);
    }

    /**
     * Handles change events in the Cluster. The current server/node will be notified of changes.
     * <p>
     * This is triggered when a server/node joins or leaves the cluster
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            // Update all the addresses in the service registry, and re-registers the current server/node for future updates
            updateAddresses();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
