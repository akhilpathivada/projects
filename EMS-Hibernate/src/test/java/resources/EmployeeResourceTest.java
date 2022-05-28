package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import ems.exceptions.EmployeeNotFound;
import ems.model.Employee;
import ems.resource.EmployeeResource;
import ems.service.DepartmentService;
import ems.service.EmployeeService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import util.ResourceTestHelper;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class EmployeeResourceTest extends BaseResourceTest{
    private static final EmployeeService EMPLOYEE_SERVICE = mock(EmployeeService.class);
    private static final DepartmentService DEPARTMENT_SERVICE = mock(DepartmentService.class);

    @ClassRule
    public static final ResourceTestRule RESOURCE_TEST_RULE
            = ResourceTestHelper.resourceTestRuleBuilder(resourceObject());

    private static DepartmentService departmentService;
    private static EmployeeService employeeService;
    private Employee inputEmployee, expectedEmployee, employeeWithNonExistingDepartmentId;
    private String BASE_URL;
    private String inputEmployeeAsJsonString, updatedEmployeeAsJsonString;

    private static EmployeeResource resourceObject() {
        return new EmployeeResource(EMPLOYEE_SERVICE, DEPARTMENT_SERVICE);
    }

    @Before
    public void setUp(){
        BASE_URL = "http://localhost:8080/employees/";
    }

    @Test
    public void shouldInvalidUrlThrowsNotFoundError() {
        Response response = resourceTestHelper.requestBuilder("/ems").get(Response.class);
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        when(EMPLOYEE_SERVICE.createEmployee(isA(Employee.class))).thenReturn(expectedEmployee);
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputEmployee));
        assertCreatedResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileCreatingEmployeeWithEmptyDetails() {
        inputEmployeeAsJsonString  = "{}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputEmployeeAsJsonString ));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileCreatingEmployeeWithEmptyName() {
        inputEmployeeAsJsonString  = "{\"name\":\"\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputEmployeeAsJsonString ));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileCreatingEmployeeWithoutDepartmentId(){
        inputEmployeeAsJsonString  = "{\"name\":\"akhil\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputEmployeeAsJsonString ));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowBadRequestWhileEmployeeCreatesWithDepartmentIdAsString() {
        final String inputEmployee = "{\"empId\":null,\"depId\":\"string\",\"name\":\"akhil\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputEmployee));
        assertBadRequestResponse(response);
    }

    @Test
    public void shouldThrowBadRequestWhileCreatingEmployeeWithNonExistingFields(){
        inputEmployeeAsJsonString = "{\"empId\":null,\"depId\":1,\"name\":\"akhil\",\"address\":null}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputEmployeeAsJsonString));
        assertBadRequestResponse(response);
    }

    @Test
    public void shouldThrowMethodNotAllowedErrorWhileCreatingEmployeeWithInvalidHttpMethod() {
        inputEmployee = new Employee(13,1,"google");
        Response response = resourceTestHelper.requestBuilder( BASE_URL + inputEmployee.getEmpId())
                .post(Entity.json(inputEmployee));
        assertMethodNotAllowedResponse(response);
    }

    @Test
    public void shouldGetEmployeeById() throws EmployeeNotFound, JsonProcessingException {
        int expectedEmployeeId = 1;
        when(EMPLOYEE_SERVICE.getEmployeeById(eq(expectedEmployeeId))).thenReturn(expectedEmployee);
        Response response = resourceTestHelper.requestBuilder(BASE_URL + expectedEmployeeId).get();
        assertOkResponse(response);
    }

    @Test
    public void shouldThrowNotFoundErrorWhileGettingNonExistingEmployee() {
        Integer nonExistingEmployeeId = 122;
        Response response = resourceTestHelper.requestBuilder(BASE_URL + "ts" + nonExistingEmployeeId).get();
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldEmployeeGetDeletedById() throws EmployeeNotFound {
        Integer employeeId = 1;
        doNothing().when(EMPLOYEE_SERVICE).deleteEmployee(eq(employeeId));
        Response response = resourceTestHelper.requestBuilder(BASE_URL + employeeId).delete();
        assertNoContentResponse(response);
    }

    @Test
    public void shouldThrowNotFoundErrorWhileDeletingNonExistingEmployee(){
        Integer deletingEmployeeId = 123;
        Response response = resourceTestHelper.requestBuilder(BASE_URL + "ts" + deletingEmployeeId).delete();
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldThrowMethodNotAllowedErrorDeletingEmployeeWithInvalidHttpMethod() {
        Response response = resourceTestHelper.requestBuilder(BASE_URL).delete();
        assertMethodNotAllowedResponse(response);
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        Integer actualEmployeeId = 1;
        Employee updatedEmployee = new Employee( 1,1,"freshworks");
        when(EMPLOYEE_SERVICE.updateEmployee(actualEmployeeId, updatedEmployee)).thenReturn(updatedEmployee);
        Response response = resourceTestHelper.requestBuilder(BASE_URL + actualEmployeeId)
                .put(Entity.json(updatedEmployee));
        assertOkResponse(response);
    }

    @Test
    public void shouldThrowNotFoundErrorWhileUpdatingNonExistingEmployee(){
        Integer updatingEmployeeId = 123;
        updatedEmployeeAsJsonString = "{\"depId\":1,\"name\":\"ramesh\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL + "ts" + updatingEmployeeId)
                .put(Entity.json(updatedEmployeeAsJsonString));
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileUpdatingEmployeeWithEmptyFields(){
        updatedEmployeeAsJsonString = "{}";
        Integer actualEmployeeId = 1;
        Response response = resourceTestHelper.requestBuilder(BASE_URL + actualEmployeeId)
                .put(Entity.json(updatedEmployeeAsJsonString));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowMethodNotAllowedErrorWhileUpdatingEmployeeWithInvalidHttpMethod() {
        String inputEmployee = "{\"depId\":1,\"name\":\"google\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).put(Entity.json(inputEmployee));
        assertMethodNotAllowedResponse(response);
    }

    @Test
    public void shouldGetSortedEmployeesBasedOnName() {
        Response response = resourceTestHelper.requestBuilder("http://localhost:8080/employees?=" + "name/").get();
        assertOkResponse(response);
    }

    @Override
    protected void populateStubData(){
        super.populateStubData();
    }

    @Override
    protected EmployeeService serviceMock(){
        return EMPLOYEE_SERVICE;
    }

    @Override
    protected ResourceTestRule getResourceTestRule(){
        return RESOURCE_TEST_RULE;
    }
}
