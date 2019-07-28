package org.tenkichannel.umbrella.carry.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.kogito.process.impl.DefaultWorkItemHandlerConfig;

public class WorkItemHandlerConfig extends DefaultWorkItemHandlerConfig {

    private final Map<String, WorkItemHandler> workItemHandlers = new HashMap<>();
    private final List<String> supportedHandlers = Arrays.asList(WeatherServiceCallWorkItemHandler.NAME);

    public WorkItemHandlerConfig() {

    }

    @Override
    public WorkItemHandler forName(String name) {
        workItemHandlers.putIfAbsent(WeatherServiceCallWorkItemHandler.NAME, new WeatherServiceCallWorkItemHandler());
        if (supportedHandlers.contains(name)) {
            return workItemHandlers.get(name);
        }
        return super.forName(name);
    }

    @Override
    public Collection<String> names() {
        List<String> names = new ArrayList<>(supportedHandlers);
        names.addAll(super.names());
        return names;
    }

}
