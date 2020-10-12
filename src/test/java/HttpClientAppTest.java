import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class HttpClientAppTest {
    public static final int PORT1 = 8088;
    public static final int PORT2 = 8089;
    public static final String WORKER_ADDRESS_1 = String.format("http://localhost:%d/task", PORT1);
    public static final String WORKER_ADDRESS_2 = String.format("http://localhost:%d/task", PORT2);

    private static WireMockServer wireMockServer1;
    private static WireMockServer wireMockServer2;

    @BeforeClass
    public static void setupServer() {
        wireMockServer1 = new WireMockServer(wireMockConfig().port(PORT1));
        wireMockServer2 = new WireMockServer(wireMockConfig().port(PORT2));
        wireMockServer1.start();
        wireMockServer2.start();
    }

    @Test
    public void sendTasksToWorkers() {
        String response1 = "12345678";
        String response2 = "910111213";
        String calcTask1 = "10940340, 23432400, 3043579358";
        String calcTask2 = "123456789, 2329372932923, 208098320921381";

        setupStubs(calcTask1, calcTask2, response1, response2);

        Aggregator aggregator = new Aggregator();

        List<String> results = aggregator.sendTasksToWorkers(
                Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2),
                Arrays.asList(calcTask1, calcTask2));

        assert (results.containsAll(Arrays.asList(response1, response2)));
    }

    private void setupStubs(String calcTask1, String calcTask2, String response1, String response2) {
        wireMockServer1.stubFor(post(urlEqualTo("/task"))
                .withRequestBody(matching(calcTask1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(response1)));

        wireMockServer1.stubFor(post(urlEqualTo("/task"))
                .withRequestBody(matching(calcTask2))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(response2)));

        wireMockServer2.stubFor(post(urlEqualTo("/task"))
                .withRequestBody(matching(calcTask1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(response1)));

        wireMockServer2.stubFor(post(urlEqualTo("/task"))
                .withRequestBody(matching(calcTask2))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(response2)));
    }

    @AfterClass
    public static void stopServers() {
        wireMockServer1.shutdown();
        wireMockServer2.shutdown();
    }
}