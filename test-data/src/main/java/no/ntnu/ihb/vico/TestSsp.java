package no.ntnu.ihb.vico;

import java.io.File;

public class TestSsp {

    public static File get(String path) {
        File projectFolder = new File(".").getAbsoluteFile();
        while (!projectFolder.getName().equals("acco")) {
            projectFolder = projectFolder.getParentFile();
        }
        return new File(projectFolder, "test-data/data/ssp" + File.separator + path).getAbsoluteFile();
    }

}
