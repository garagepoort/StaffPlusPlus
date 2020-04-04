package net.shortninja.staffplus.server.hook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HookHandler {

    private final Set<IHook> hooks = new HashSet<>();

    public HookHandler() {
    }

    public void addHook(IHook hook) {
        hooks.add(hook);
    }

    public void removeHook(IHook hook) {
        hooks.remove(hook);
    }

    public boolean hasHook(String name, String version) {
        return hooks.stream().anyMatch(hook -> hook.getPluginName().equalsIgnoreCase(name) && hook.getPluginVersion().equals(version));
    }

    public boolean hasHook(String name) {
        return hooks.stream().anyMatch(hook -> hook.getPluginName().equalsIgnoreCase(name));
    }

    public Set<IHook> getHooks() {
        return new HashSet<>(hooks);
    }

    public void enableAll() {
        for (IHook hook : hooks) {
            if (hook.canHook()) {
                hook.onEnable();
            }
        }
    }

    public void disableAll() {
        final List<IHook> hooks = new ArrayList<>(this.hooks);

        for (int i = hooks.size() - 1; i > 0; i--) {
            IHook hook = hooks.get(i);

            if (hook.canHook()) {
                hook.onDisable();
            }
        }
    }
}
