package com.example.weatherg.transport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class StorageService {
    public static void saveCity(String city) {
        try (FileWriter writer = new FileWriter("city.txt", false)) {
            writer.write(city);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getCity() {
        try (FileReader reader = new FileReader("city.txt")) {
            char[] buf = new char[128];
            int c;
            while ((c = reader.read(buf)) > 0) {
                if (c < 128) {
                    buf = Arrays.copyOf(buf, c);
                }
            }
            return new String(buf);
        } catch (IOException e) {
            File f = new File("city.txt");
            return null;
        }
    }
}
