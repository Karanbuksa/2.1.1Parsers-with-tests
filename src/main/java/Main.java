import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csvToBean.parse();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String string) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int index = string.indexOf('['); index >= 0; index = string.indexOf('[', index + 1)) {
            indexes.add(index);
        }
        for (int index = string.indexOf('{'); index >= 0; index = string.indexOf('{', index + 1)) {
            indexes.add(index);
        }
        for (int index = string.indexOf(','); index >= 0; index = string.indexOf(',', index + 1)) {
            indexes.add(index + 1);
        }
        for (int index = string.indexOf(']'); index >= 0; index = string.indexOf(']', index + 1)) {
            indexes.add(index);
        }
        for (int index = string.indexOf('}'); index >= 0; index = string.indexOf('}', index + 1)) {
            indexes.add(index);
        }

        List<Integer> ind = indexes.stream()
                .sorted(Comparator.reverseOrder()).toList();
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < indexes.size() - 1; i++) {
            stringBuilder.insert(ind.get(i), "\n");
        }

        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

