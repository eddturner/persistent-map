package uk.ac.edd.persistence;

import net.openhft.chronicle.map.ChronicleMap;

import java.io.File;
import java.io.IOException;

/**
 * Created 11/12/18
 *
 * @author Edd
 */
public class DemoPersistence {

    private static final int LOG_INTERVAL = 1000000;

    public static void main(String[] args) throws IOException {
        File persistenceFile = new File("chronicle.map");
        int maxEntries = 200000000;
        int entryCount = 20000000;
        ChronicleMap<String, String> uniProtUniRefMap = ChronicleMap
                .of(String.class, String.class)
                .averageKey("P1234567777")
                .averageValue("UniRef100_P1234567777")
                .entries(maxEntries)
                .createPersistedTo(persistenceFile);

        int count = 0;
        for (int i = 0; i < entryCount; i++) {
            uniProtUniRefMap.put("P1234" + i, "UniRef90_P1234" + i);
            if (count++ % LOG_INTERVAL == 0) {
                System.out.println("Entries: " + count);
            }
        }

//        uniProtUniRefMap
//                .forEach((key, value) ->
//                                 System.out.println("[" + key + ", " + value + "]"));
    }
}
