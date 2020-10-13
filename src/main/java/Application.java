import java.util.Arrays;
import java.util.List;

public class Application {

    public static final String WORKER_ADDRESS_1="http://localhost:8001/task";
    public static final String WORKER_ADDRESS_2="http://localhost:8002/task";

    public static void main(String[] args){
        Aggregator aggregator = new Aggregator();

        String task1="10,200";
        String task2="10,300";

        List<String> workers = Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2);
        List<String> tasks = Arrays.asList(task1, task2);

        List<String> results = aggregator.sendTasksToWorkers(workers, tasks);

        for(String result : results){
            System.out.println(result);
        }
    }
}
