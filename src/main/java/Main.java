import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, "data2.json");
        String json3 = readString("data2.json");
        for (Employee employee : jsonToList(json3)) {
            System.out.println(employee);
        }
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
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String string, String name) {
        try (FileWriter fileWriter = new FileWriter(name)) {
            fileWriter.write(string);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String string) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(string));
        Node root = doc.getDocumentElement();
        List<Employee> employeeList = new ArrayList<>();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                NodeList contents = element.getChildNodes();
                List<String> values = new ArrayList<>();
                for (int a = 0; a < contents.getLength(); a++) {
                    if (contents.item(a).getTextContent() != null && !contents.item(a).getTextContent().contains("\n"))
                        values.add(contents.item(a).getTextContent());
                }//TODO: ????????????????????, ?????????????????? ??????. ??????????, ???????? ?????????? ?????????????? ???????????? ?????????????????? ????????????
                if (values.size() != 0) {
                    Employee employee = new Employee();
                    employee.id = Long.parseLong(values.get(0));
                    employee.firstName = values.get(1);
                    employee.lastName = values.get(2);
                    employee.country = values.get(3);
                    employee.age = Integer.parseInt(values.get(4));
                    employeeList.add(employee);
                }
            }
        }
        return employeeList;
    }

    public static String readString(String fileName) throws RuntimeException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<Employee> jsonToList(String fileName) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<Employee> employeeList = new ArrayList<>();
        try {
            Object object = jsonParser.parse(fileName);
            jsonArray = (JSONArray) object;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Object object : jsonArray) {
            Employee employee = gson.fromJson(object.toString(), Employee.class);
            employeeList.add(employee);

        }
        return employeeList;
    }
}