package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;

import java.util.Arrays;
import java.util.List;

public class CommaSplitTransformer implements IConfigTransformer<List<String>, String> {
    @Override
    public List<String> mapConfig(String s) {
        return Arrays.asList(s.split("\\s*,\\s*"));
    }
}
