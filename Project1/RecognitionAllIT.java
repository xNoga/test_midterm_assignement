package net.sf.javaanpr.test;

import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class RecognitionAllIT {

    @Parameterized.Parameters(name = "{index}: plate{1}")
    public static Collection<Object[]> data() throws IOException {
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));

        Properties properties = new Properties();
        properties.load(resultsStream);
        resultsStream.close();
        assertTrue(properties.size() > 0);

        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();
        assertNotNull(snapshots);
        assertTrue(snapshots.length > 0);

        ArrayList<Object[]> params = new ArrayList<>();

        for (File snap : snapshots) {
            String snapName = snap.getName();
            String plateCorrect = properties.getProperty(snapName);
            params.add(new Object[]{snap, plateCorrect});
        }

        return params;
    }

    private File input;
    private String expected;

    public RecognitionAllIT (File input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    private Intelligence intel;

    @Before
    public void before() throws IOException, SAXException, ParserConfigurationException {
        intel = new Intelligence();
    }

    @Test
    public void allTester() throws ParserConfigurationException, SAXException, IOException {
        // assertEquals(expected, scanPlate(input));
        assertThat(expected, equalTo(scanPlate(input))); // using Hamcrest here
    }

    public String scanPlate (File snap) throws IOException, ParserConfigurationException, SAXException {
        CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snap));
        return intel.recognize(carSnap, false);
    }
}
