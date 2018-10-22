package net.shortninja.staffplus.server.data.file;

import net.shortninja.staffplus.StaffPlus;

import java.io.*;

public class ChangelogFile {
    public ChangelogFile() {
        try {
            copyFile();
        } catch (IOException | NullPointerException exception) {
            StaffPlus.get().message.sendConsoleMessage("Error occurred while copying 'changelog.txt'!", true);
        }
    }

    private void copyFile() throws IOException, NullPointerException {
        File file = new File(StaffPlus.get().getDataFolder(), "changelog.txt");
        InputStream in = this.getClass().getResourceAsStream("/changelog.txt");
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int current;

        if (!file.exists()) {
            StaffPlus.get().getDataFolder().mkdirs();
            file.createNewFile();
        }

        while ((current = in.read(buffer)) > -1) {
            out.write(buffer, 0, current);
        }

        in.close();
        out.close();
    }
}