package com.example.weatherg.transport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class StorageService {

    /*
     * Сохраняет название города в файл "city.txt".
     * @param city Название города для сохранения.
     */
    public static void saveCity(String city) {
        // Используем try-with-resources для автоматического закрытия FileWriter
        try (FileWriter writer = new FileWriter("city.txt", false)) {
            // Записываем название города в файл
            writer.write(city);
            // Принудительно записываем все данные в файл
            writer.flush();
        } catch (IOException e) {
            // Выводим сообщение об ошибке в случае исключения
            System.out.println(e.getMessage());
        }
    }

     /*
     * Читает название города из файла "city.txt".
     * @return Название города, или null, если произошла ошибка.
     */
    public static String getCity() {
        // Используем try-with-resources для автоматического закрытия FileReader
        try (FileReader reader = new FileReader("city.txt")) {
            // Создаем буфер для чтения из файла
            char[] buf = new char[128];
            // Переменная для хранения количества считанных символов
            int c;
            // Читаем данные из файла, пока не достигнем конца файла
            while ((c = reader.read(buf)) > 0) {
                // Если количество считанных символов меньше размера буфера,
                // то обрезаем буфер до фактического количества считанных символов
                if (c < 128) {
                    buf = Arrays.copyOf(buf, c);
                }
            }
            // Создаем строку из символьного массива
            return new String(buf);
        } catch (IOException e) {
            // В случае ошибки при чтении файла, создаем объект File
            File f = new File("city.txt");
            return null;
        }
    }
}
