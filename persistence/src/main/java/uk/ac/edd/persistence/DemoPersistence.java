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
        int entryCount;
        String dirPath;
        if (args.length > 0 && !args[0].equals("") && !args[1].equals("")) {
            entryCount = Integer.parseInt(args[0]);
            dirPath = args[1];
        } else {
            entryCount = 2000000;
            dirPath = ".";
        }

        String path = dirPath + "/chronicle-" + System.currentTimeMillis() + ".map";
        File persistenceFile = new File(path);
        int maxEntries = 200000000;
        System.out.println("Creating map in: " + path);
        System.out.println(" ... of size: " + entryCount);
        ChronicleMap<String, String> uniProtUniRefMap = ChronicleMap
                .of(String.class, String.class)
                .averageKey("P1234567777")
                .averageValue("UniRef100_P1234567777")
                .entries(maxEntries)
                .createPersistedTo(persistenceFile);

        int count = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < entryCount; i++) {
            uniProtUniRefMap.put("P1234" + i, "UniRef90_P1234" + i);
            if (count++ % LOG_INTERVAL == 0) {
                double rate = getRate(count, start);

                System.out.println("Entries: " + count);
                System.out.println("Rate: " + rate);
                System.out.println("--------");
            }
        }

        double rate = getRate(count, start);
        System.out.println("Host: " + System.getenv("HOSTNAME"));
        System.out.println("File written to: " + uniProtUniRefMap.file().getAbsolutePath());
        System.out.println("File exists: " + uniProtUniRefMap.file().exists());
        System.out.println("Overall rate: " + rate);
        System.out.println("Map size is: " + uniProtUniRefMap.size());

//        uniProtUniRefMap
//                .forEach((key, value) ->
//                                 System.out.println("[" + key + ", " + value + "]"));
    }

    private static double getRate(double count, long start) {
        long now = System.currentTimeMillis();
        double duration = (now - start) / 1000.0;
        return count / duration;
    }
}
