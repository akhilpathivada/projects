package model;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ems.model.Employee;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    private Employee employee;
    private String BASE_PATH;

    @Before
    public void setUpEmployee(){
        employee = new Employee( 1, 1, "mahesh");
        BASE_PATH = "src/test/java/fixtures/";
    }

    @Test
    public void shouldGetEmployeeName(){
        assertEquals("mahesh", employee.getName());
    }

    @Test
    public void shouldGetEmployeeId(){
        employee.setEmpId(1);
        assertEquals((Integer)1, employee.getEmpId());
    }

    @Test
    public void shouldGetDepartmentIdOfEmployee(){
        assertEquals((Integer)1, employee.getDepId());
    }

    @Test
    public void shouldSetEmpId(){
        employee.setEmpId(2);
        assertEquals((Integer) 2, employee.getEmpId());
    }

    @Test
    public void shouldSetEmployeeName(){
        employee.setName("Ravindra");
        assertEquals("Ravindra", employee.getName());
    }

    @Test
    public void shouldEmployeeSerializesToJson() throws Exception {
        final Employee actualEmployee = new Employee(1,1, "akhil");
        final String expectedEmployee = "{\"empId\":1,\"name\":\"akhil\",\"depId\":1}";
        String jsonString = MAPPER.writeValueAsString(actualEmployee);
        assertEquals(jsonString, expectedEmployee);
    }

    @Test
    public void shouldJsonDeserializesToEmployee() throws IOException {
        File file = new File(BASE_PATH + "EmployeeBean.json");
        final Employee expectedEmployee = new Employee(1, 23, "akhil");
        Employee actualEmployee = MAPPER.readValue(file, Employee.class);
        assertTrue(EqualsBuilder.reflectionEquals(expectedEmployee, actualEmployee));
    }

    @Test(expected = JsonMappingException.class)
    public void shouldJsonDeserializesToEmployeeWithInvalidParameters() throws IOException {
        File file = new File(BASE_PATH + "EmployeeBeanWithInvalid.json");
        MAPPER.readValue(file, Employee.class);
    }
}
