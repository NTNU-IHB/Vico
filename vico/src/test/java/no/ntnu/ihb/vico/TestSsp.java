package no.ntnu.ihb.vico;

import java.io.File;

public class TestSsp {

    public static File get(String path) {
        return new File("src/test/resources/ssp", path).getAbsoluteFile();
    }

}
