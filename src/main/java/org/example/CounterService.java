package org.example;

import org.apache.commons.codec.digest.MurmurHash3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;

public class CounterService {

    public void calcUniqueCount(String fileName, int precision) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            System.out.println("Input file: " + fileName);

            UniqueCounter<String> counter;
            if (precision > 0) {
                counter = new Hll32Counter<>(precision, CounterService::hashCode);
                System.out.println("Precision: " + precision);
            } else {
                counter = new Hll32Counter<>();
                System.out.println("Precision is default");
            }
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

    public static long hashCode(String s) {
        return MurmurHash3.hash64(s.getBytes());
    }

    private void writeSate(long lineCounter, UniqueCounter<String> counter) {
        System.out.println(LocalDateTime.now() + " input count: " + lineCounter
                + "\n                             "
                + "unique count: " + counter.getUniqueCount() + "\n");
    }
}
