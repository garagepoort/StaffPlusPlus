package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.common.JavaUtils;

import java.util.List;

public class MessageMultiLineTransformer implements IConfigTransformer<List<String>, String> {
    @Override
    public List<String> mapConfig(String value) {
        return JavaUtils.stringToList(value);
    }
}
