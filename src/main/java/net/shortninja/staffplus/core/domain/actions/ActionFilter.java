package net.shortninja.staffplus.core.domain.actions;

import java.util.Map;

public interface ActionFilter {

    boolean isValidAction(CreateStoredCommandRequest createStoredCommandRequest, Map<String, String> filters);
}
