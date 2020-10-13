import networking.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Aggregator {

    private WebClient webclient;

    public Aggregator(){
        this.webclient=new WebClient();
    }

    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) {
        CompletableFuture<String>[] futures = new CompletableFuture[workersAddresses.size()];

        for(int i=0; i <workersAddresses.size(); i++){
            String workerAddress = workersAddresses.get(i);
            String task=tasks.get(i);

            byte[] requestPayload = task.getBytes();

            CompletableFuture<String> future =webclient.sendTask(workerAddress, requestPayload);

            futures[i]=future;
        }

        List<String> results = new ArrayList<>();

        for(int i=0; i<futures.length; i++){
            results.add(futures[i].join());
        }

        return results;
    }


}
