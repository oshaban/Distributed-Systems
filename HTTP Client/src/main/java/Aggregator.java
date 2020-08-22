import networking.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Aggregator {

    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    /**
     * Sends tasks to workers and aggregates the responses
     * @param workersAddresses List of worker addresses
     * @param tasks List of tasks
     * @return
     */
    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) {

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < workersAddresses.size(); i++) {
            String workerAddress = workersAddresses.get(i);
            String task = tasks.get(i);

            byte[] requestPayload = task.getBytes();

            CompletableFuture<String> futureResult = this.webClient.sendTask(workerAddress, requestPayload);
            futures.add(futureResult);
        }

        // Blocks and waits for all HTTP responses from workers to finish
        List<String> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        return results;
    }

}
