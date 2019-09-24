package org.tenkichannel.forecast.rain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.inject.Inject;
import javax.inject.Named;

import io.quarkus.test.junit.QuarkusTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.Model;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class RainForecastProcessTest {


    @Inject
    @Named("tenkichannel.rainforecast")
    Process<? extends Model> rainForecastProcess;
    
    @BeforeAll
    public static final void init() {
        System.setProperty("local", "true");
    }
    
    @BeforeEach
    public void setup() {
        // abort all intsances after each test
        // as other tests might have added instances
        // needed until Quarkust implements @DirtiesContext similar to springboot
        // see https://github.com/quarkusio/quarkus/pull/2866
        rainForecastProcess.instances().values().forEach(pi -> pi.abort());
    }
    
    @Test
    public void testRainForecastProcess() throws IOException {
        assertNotNull(rainForecastProcess);
        
        try (final MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(readResource("/mock-responses/weather-api/rain.json")));
            server.start(8081);
            
            // create the input parameters
            final Model model = rainForecastProcess.createModel();
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("location", new Location("New York", "US", null, null));
            model.fromMap(parameters);
            
            ProcessInstance<?> processInstance = rainForecastProcess.createInstance(model);
            processInstance.start();
            
            assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.status());
            Model result = (Model)processInstance.variables();
            assertEquals(3, result.toMap().size());
            assertTrue(((Result)result.toMap().get("result")).isRain());
        }
    }
    
    private static String readResource(final String path) {
        try (Scanner s = new Scanner(RainForecastProcessTest.class.getResourceAsStream(path))) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }
    
}
