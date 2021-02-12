package net.shortninja.staffplus.common.actions.database;

import net.shortninja.staffplus.unordered.Actionable;
import net.shortninja.staffplus.common.actions.ExecutableActionEntity;

import java.util.List;

public interface ActionableRepository {

    List<ExecutableActionEntity> getActions(Actionable actionable);

    int saveActionable(ExecutableActionEntity executableActionEntity);

    void markExecuted(int executableActionId);

    void markRollbacked(int executableActionId);
}
