package no.ntnu.ihb.vico;

import java.io.File;

public class TestFmus {

    public static File get(String path) {
        return new File("../test-fmus/", path).getAbsoluteFile();
    }

}
