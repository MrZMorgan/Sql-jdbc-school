package ua.com.foxminded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DataGenerator {

    public List<String> readFile(String fileName) {
        List<String> strings = new LinkedList<>();

        try {
            strings = Files.lines(Paths.get(fileName)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }
}
