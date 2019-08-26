/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.overbeck.petscanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.overbeck.petscanner.model.Dog;
import org.overbeck.petscanner.model.Input;

public class App {

    static Map<String, Set<Dog>> knownDogs = new HashMap<>();

    public static void main(String[] args) throws IOException {
        final JsonReader jsonReader = new JsonReader(getInputReader(args));
        final Gson gson = new Gson();
        final Input input = gson.fromJson(jsonReader, Input.class);
        final Timer timer = new Timer();
        final PetScannerTask petScannerTask = new PetScannerTask(input);
        timer.schedule(petScannerTask, 0, TimeUnit.MINUTES.toMillis(Integer.parseInt(input.options.frequencyInMinutes)));
    }

    private static Reader getInputReader(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            return new FileReader(args[0]);
        }
        return new InputStreamReader(App.class.getResourceAsStream("/input.json"));
    }

    static String baseUrl(String url) {
        try {
            final URL url1 = new URL(url);
            return url1.getProtocol() + "://" + url1.getHost() + "/";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
