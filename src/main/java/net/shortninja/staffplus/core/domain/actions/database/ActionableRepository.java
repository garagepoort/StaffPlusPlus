package net.shortninja.staffplus.core.domain.actions.database;

import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;
import net.shortninja.staffplusplus.Actionable;

import java.util.List;

public interface ActionableRepository {

    List<ExecutableActionEntity> getActions(Actionable actionable);

    int saveActionable(ExecutableActionEntity executableActionEntity);

    void markExecuted(int executableActionId);

    void markRollbacked(int executableActionId);

    void delete(Actionable actionable);
}
