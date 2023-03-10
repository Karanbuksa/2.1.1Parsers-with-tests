import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParsersTests {
    List<Employee> expectedEmployees = new ArrayList<>();
    @BeforeEach
    public void prepare() {
        expectedEmployees.add(new Employee(1, "John", "Smith", "USA", 25));
        expectedEmployees.add(new Employee(2, "Ivan", "Petrov", "RU", 23));
        expectedEmployees.add(new Employee(3, "David", "Tenant", "UK", 45));
        expectedEmployees.add(new Employee(4, "Math", "Smith", "UK", 38));
    }

    @Test
    public void parseXML_test() throws ParserConfigurationException, IOException, SAXException {
        String data = "src/test/resources/data.xml";

        List<Employee> employees = Main.parseXML(data);

        Assertions.assertEquals(expectedEmployees.toString(), employees.toString());

    }

    @Test
    public void parseCSV_test() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String data = "src/test/resources/data.csv";


        List<Employee> employees = Main.parseCSV(columnMapping, data);

        Assertions.assertEquals(expectedEmployees.toString(), employees.toString());


    }

    @Test
    public void readString_test() {
        String data = "src/test/resources/data.json";


        List<Employee> employees = Main.jsonToList(Main.readString(data));

        Assertions.assertEquals(expectedEmployees.toString(), employees.toString());


    }

    @Test
    public void jsonToList_employeeList_size_test(){
        String data = "src/test/resources/data.json";

        List<Employee> employees = Main.jsonToList(Main.readString(data));

        Assertions.assertEquals(employees.size(), 4);
    }
}
