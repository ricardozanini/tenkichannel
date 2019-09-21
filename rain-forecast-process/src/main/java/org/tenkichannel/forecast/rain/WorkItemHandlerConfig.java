package org.tenkichannel.forecast.rain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.kogito.process.impl.DefaultWorkItemHandlerConfig;

@ApplicationScoped
public class WorkItemHandlerConfig extends DefaultWorkItemHandlerConfig {
    private final Map<String, WorkItemHandler> workItemHandlers = new HashMap<>();
    private final List<String> supportedHandlers = Arrays.asList(WeatherServiceCallWorkItemHandler.WORK_ITEM_NAME);

    @Override
    public WorkItemHandler forName(String name) {
        workItemHandlers.putIfAbsent(WeatherServiceCallWorkItemHandler.WORK_ITEM_NAME, new WeatherServiceCallWorkItemHandler());
        if (supportedHandlers.contains(name)) {
            return workItemHandlers.get(WeatherServiceCallWorkItemHandler.WORK_ITEM_NAME);
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