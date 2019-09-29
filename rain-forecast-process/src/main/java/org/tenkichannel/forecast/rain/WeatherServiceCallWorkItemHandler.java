package org.tenkichannel.forecast.rain;

import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.kogito.cloud.workitems.DiscoveredServiceWorkItemHandler;
import org.kie.kogito.cloud.workitems.HttpMethods;
import org.kie.kogito.cloud.workitems.ServiceInfo;

public class WeatherServiceCallWorkItemHandler extends DiscoveredServiceWorkItemHandler {

    public static final String WORK_ITEM_NAME = "WeatherServiceCall";

    public WeatherServiceCallWorkItemHandler() {
        if ("true".equalsIgnoreCase(System.getProperty("local"))) {
            this.addServices("forecast", new ServiceInfo("http://localhost:8081/forecast", null));
        }
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        Map<String, Object> results = discoverAndCall(workItem, System.getenv("NAMESPACE"), "Service",
                HttpMethods.POST);
        // produce the expected objects to be validated by the next nodes
        results.put("result", new Result());
        results.put("weather", new Weather(results.get("condition").toString()));
        // return
        manager.completeWorkItem(workItem.getId(), results);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // nothing
    }

    @Override
    public String getName() {
        return WORK_ITEM_NAME;
    }

}
