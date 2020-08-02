package no.ntnu.ihb.vico;

import java.io.File;

public class TestFmus {

    public static File get(String path) {
        File projectFolder = new File(".").getAbsoluteFile();
        while (!projectFolder.getName().equals("vico")) {
            projectFolder = projectFolder.getParentFile();
        }
        return new File(projectFolder, "test-data/data/fmus" + File.separator + path).getAbsoluteFile();
    }

}
