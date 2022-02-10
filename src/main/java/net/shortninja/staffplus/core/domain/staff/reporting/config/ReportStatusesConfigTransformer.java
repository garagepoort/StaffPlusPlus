package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplusplus.reports.ReportStatus;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ReportStatusesConfigTransformer implements IConfigTransformer<List<ReportStatus>, String> {
    @Override
    public List<ReportStatus> mapConfig(String value) {
        return stream(value.split(";"))
            .filter(s -> !s.isEmpty())
            .map(ReportStatus::valueOf)
            .collect(Collectors.toList());
    }
}
