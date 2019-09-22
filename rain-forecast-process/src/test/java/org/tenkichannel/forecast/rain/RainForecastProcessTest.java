package org.tenkichannel.forecast.rain;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.quarkus.test.junit.QuarkusTest;
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
    public void testRainForecastProcess() {
        assertNotNull(rainForecastProcess);
        
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
