package net.shortninja.staffplus.unordered;

import java.util.UUID;

public interface IReport {

	String getReason();

	String getReporterName();

	UUID getReporterUuid();

	void setReporterName(String newName);

	UUID getUuid();
}
