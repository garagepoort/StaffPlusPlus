package net.shortninja.staffplus.core.stafflocations.gui;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplusplus.stafflocations.StaffLocationFilters.StaffLocationFiltersBuilder;

import java.util.Arrays;
import java.util.List;

@IocBean
public class StaffLocationFiltersMapper {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CREATOR = "creator";

    public List<String> getFilterKeys() {
        return Arrays.asList(ID, NAME, CREATOR);
    }

    public void map(String key, String value, StaffLocationFiltersBuilder filtersBuilder) {
        if (key.equalsIgnoreCase(ID)) {
            filtersBuilder.id(Integer.parseInt(value));
        }
        if (key.equalsIgnoreCase(NAME)) {
            filtersBuilder.name(value);
        }
        if (key.equalsIgnoreCase(CREATOR)) {
            filtersBuilder.creator(value);
        }
    }
}
