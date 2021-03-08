package be.garagepoort.staffplusplus.discord.common;

import static java.io.File.separator;

public class TemplateFile {

    private final String id;
    private final String directory;
    private final String file;

    public TemplateFile(String directory, String file) {
        this.directory = directory;
        this.file = file;
        this.id = directory + "/" + file;
    }

    public String getId() {
        return id;
    }

    public String getResourcePath(String templatePack) {
        return "discordtemplates" + separator + templatePack + separator + directory + separator + file + ".json";
    }

}