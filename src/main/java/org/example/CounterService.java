package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;

public class CounterService {

    public void calcUniqueCount(String fileName, int precision) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            System.out.println("Input file: " + fileName);

            UniqueCounter<String> counter = new Hll32Counter<>(precision);
            System.out.println("Precision: " + precision);

            long lineCounter = 0L;

            String inputAddress = reader.readLine();
            lineCounter++;
            long degree = 10_00_000;

            while (inputAddress != null) {
                counter.add(inputAddress);
                inputAddress = reader.readLine();

                if (lineCounter == degree) {
                    writeSate(lineCounter, counter);
                    degree = degree * 2;
                }
                lineCounter++;
            }
            writeSate(lineCounter, counter);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void writeSate(long lineCounter, UniqueCounter<String> counter) {
        System.out.println(LocalDateTime.now() + " input count: " + lineCounter
                + "\n                             "
                + "unique count: " + counter.getUniqueCount() + "\n");
    }
}
