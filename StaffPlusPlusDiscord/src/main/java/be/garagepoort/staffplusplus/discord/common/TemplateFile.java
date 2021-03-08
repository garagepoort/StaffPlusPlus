package be.garagepoort.staffplusplus.discord.common;
import static java.io.File.separator;

public class TemplateFile {

    private final String id;

    public TemplateFile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getResourcePath(String templatePack) {
        return "discordtemplates" + separator + templatePack + separator + id + ".json";
    }

}