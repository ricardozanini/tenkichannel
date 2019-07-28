package org.tenkichannel.umbrella.carry.process;

import java.util.Collections;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.kogito.cloud.workitems.DiscoveredServiceWorkItemHandler;
import org.kie.kogito.cloud.workitems.HttpMethods;

public class WeatherServiceCallWorkItemHandler extends DiscoveredServiceWorkItemHandler {

    public static final String NAME = "WeatherServiceCall";

    private String namespace = System.getenv("NAMESPACE");

    public WeatherServiceCallWorkItemHandler() {}

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        Map<String, Object> results = discoverAndCall(workItem, namespace, "ServiceCall", HttpMethods.POST);
        final Weather weather = new Weather();
        weather.setCondition(results.get("condition").toString());
        manager.completeWorkItem(workItem.getId(), Collections.singletonMap("weather", weather));
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

    }

    @Override
    public String getName() {
        return NAME;
    }

}
