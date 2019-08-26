package org.overbeck.petscanner;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.junit.Test;

public class PetScannerTaskTest {

    @Test
    public void testIndexes() throws IOException {
        final InputStream stream = PetScannerTaskTest.class.getResourceAsStream("/salinasbasedmanyshelters.html");
        final Document document = Jsoup.parse(stream, null, "http://petharbor.com");
        final PetScannerTask.Indexes indexes = PetScannerTask.calculateIndexes(document.selectFirst("table.ResultsTable"));
        assertEquals(new Integer(0), indexes.DETAILS_INDEX);
        assertEquals(null, indexes.ID_INDEX);
        assertEquals(new Integer(1), indexes.NAME_INDEX);
        assertEquals(new Integer(2), indexes.GENDER_INDEX);
        assertEquals(new Integer(3), indexes.COLOR_INDEX);
        assertEquals(new Integer(4), indexes.BREED_INDEX);
        assertEquals(new Integer(5), indexes.AGE_INDEX);
        assertEquals(null, indexes.WEIGHT_INDEX);
        assertEquals(new Integer(6), indexes.TIME_AT_SHELTER_INDEX);
    }

    @Test
    public void testSantaCruzIndexes() throws IOException {
        final InputStream stream = PetScannerTaskTest.class.getResourceAsStream("/santacruzshelter.html");
        final Document document = Jsoup.parse(stream, null, "http://petharbor.com");
        final PetScannerTask.Indexes indexes = PetScannerTask.calculateIndexes(document.selectFirst("table.ResultsTable"));
        assertEquals(new Integer(0), indexes.DETAILS_INDEX);
        assertEquals(new Integer(1), indexes.ID_INDEX);
        assertEquals(new Integer(2), indexes.NAME_INDEX);
        assertEquals(new Integer(3), indexes.GENDER_INDEX);
        assertEquals(new Integer(4), indexes.COLOR_INDEX);
        assertEquals(new Integer(5), indexes.BREED_INDEX);
        assertEquals(new Integer(6), indexes.AGE_INDEX);
        assertEquals(new Integer(7), indexes.WEIGHT_INDEX);
        assertEquals(null, indexes.TIME_AT_SHELTER_INDEX);
    }
}
