package model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ems.model.Department;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DepartmentTest {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    private Department department;
    private String BASE_PATH;

    @Before
    public void setUp(){
        department = new Department( 1, "google");
        BASE_PATH = "src/test/java/fixtures/";
    }

    @Test
    public void shouldGetDepartmentId(){
        department.setDepId(1);
        assertEquals((Integer) 1, department.getDepId());
    }

    @Test
    public void shouldGetEmployeeName(){
        assertEquals("google", department.getName());
    }

    @Test
    public void shouldSetDepartmentId(){
        department.setDepId(2);
        assertEquals((Integer) 2, department.getDepId());
    }

    @Test
    public void shouldSetEmployeeName(){
        department.setName("Microsoft");
        assertEquals("Microsoft", department.getName());
    }

    @Test
    public void shouldDepartmentSerializesToJson() throws Exception {
        final Department actualDepartment = new Department(1, "google");
        final String expectedDepartment = "{\"depId\":1,\"name\":\"google\"}";
        String jsonString = MAPPER.writeValueAsString(actualDepartment);
        assertEquals(jsonString, expectedDepartment);
    }

    @Test
    public void shouldJsonDeserializesToDepartment() throws IOException {
        File file = new File(BASE_PATH + "DepartmentBean.json");
        final Department expectedDepartment = new Department(2, "amazon");
        Department actualDepartment = MAPPER.readValue(file , Department.class);
        assertTrue(EqualsBuilder.reflectionEquals(expectedDepartment, actualDepartment));
    }

    @Test(expected = JsonMappingException.class)
    public void shouldJsonDeserializesToDepartmentWithInvalidParameters() throws IOException {
        File file = new File(BASE_PATH + "DepartmentBeanWithInvalid.json");
        MAPPER.readValue(file , Department.class);
    }
}
