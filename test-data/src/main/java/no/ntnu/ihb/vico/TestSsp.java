package no.ntnu.ihb.vico;

import java.io.File;

public class TestSsp {

    public static File get(String path) {
        return new File("../test-data/data/ssp", path).getAbsoluteFile();
    }

}
