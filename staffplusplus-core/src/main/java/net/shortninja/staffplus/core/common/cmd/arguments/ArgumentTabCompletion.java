package net.shortninja.staffplus.core.common.cmd.arguments;

import java.util.List;

public interface ArgumentTabCompletion {

    List<String> complete(String currentArg);
}
