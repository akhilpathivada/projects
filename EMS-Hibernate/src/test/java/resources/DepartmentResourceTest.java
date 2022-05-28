package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import ems.exceptions.DepartmentNotFound;
import ems.model.Department;
import ems.model.Employee;
import ems.resource.DepartmentResource;
import ems.service.DepartmentService;
import ems.service.EmployeeService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import util.ResourceTestHelper;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Collections;
import static org.mockito.Mockito.*;

public class DepartmentResourceTest extends BaseResourceTest{
    private static final DepartmentService DEPARTMENT_SERVICE = mock(DepartmentService.class);

    @ClassRule
    public static final ResourceTestRule RESOURCE_TEST_RULE
            = ResourceTestHelper.resourceTestRuleBuilder(resourceObject());

    private String BASE_URL;
    private static EmployeeService employeeService;
    private static DepartmentService departmentService;
    private Department inputDepartment, expectedDepartment;
    private String inputDepartmentAsJsonString, updatedDepartmentAsJsonString;

    private static DepartmentResource resourceObject() {
        return new DepartmentResource(DEPARTMENT_SERVICE);
    }

    @Test
    public void shouldThrowsNotFoundErrorWithInvalidUrl() {
        Response response = resourceTestHelper.requestBuilder("ems").get(Response.class);
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldCreateDepartment() throws JsonProcessingException, SQLException {
        when(DEPARTMENT_SERVICE.createDepartment(inputDepartment)).thenReturn(expectedDepartment);
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputDepartment));
        assertCreatedResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileCreatingDepartmentWithEmptyName(){
        inputDepartmentAsJsonString = "{\"name\":\"\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputDepartmentAsJsonString));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileCreatingDepartmentWithEmptyFields(){
        inputDepartmentAsJsonString = "{}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputDepartmentAsJsonString));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowBadRequestWhileCreatingDepartmentWithNonExistingFields(){
        final String inputDepartment = "{\"depId\":null,\"name\":\"google\",\"age\":null}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL).post(Entity.json(inputDepartment));
        assertBadRequestResponse(response);
    }

    @Test
    public void shouldThrowMethodNotAllowedErrorWhileCreatingDepartmentWithInvalidHttpMethod() {
        inputDepartment = new Department(1,"google");
        Response response = resourceTestHelper.requestBuilder(BASE_URL + inputDepartment.getDepId())
                                                .post(Entity.json(inputDepartment));
        assertMethodNotAllowedResponse(response);
    }

    @Test
    public void shouldGetDepartment() throws JsonProcessingException, DepartmentNotFound {
        int expectedDepartmentId = 1;
        when(DEPARTMENT_SERVICE.getDepartmentById(eq(expectedDepartmentId))).thenReturn(expectedDepartment);
        Response response = resourceTestHelper.requestBuilder(BASE_URL + expectedDepartmentId).get();
        assertOkResponse(response);
    }

    @Test
    public void shouldThrowNotFoundErrorWhileGettingDetailsOfNonExistingDepartment(){
        Integer expectedDepartmentId = 112;
        Response response = resourceTestHelper.requestBuilder(BASE_URL + "ts" + expectedDepartmentId).get();
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldGetEmployeesOfADepartment() throws Exception {
        Integer expectedDepartmentId = 2;
        when(DEPARTMENT_SERVICE.getEmployeesOfDepartment(eq(expectedDepartmentId)))
                .thenReturn(Collections.singletonList(new Employee()));
        Response response = resourceTestHelper.requestBuilder(BASE_URL + expectedDepartmentId + "/employees").get();
        assertOkResponse(response);
    }

    @Test
    public void shouldDeleteDepartment() throws DepartmentNotFound {
        int removingDepartmentId = 1;
        doNothing().when(DEPARTMENT_SERVICE).deleteDepartment(removingDepartmentId);
        Response response = resourceTestHelper.requestBuilder(BASE_URL + removingDepartmentId).delete();
        assertNoContentResponse(response);
    }

    @Test
    public void shouldThrowDepartmentFoundErrorWhileDeletingANonExistingDepartment() {
        Integer removingDepartmentId = 123;
        Response response = resourceTestHelper.requestBuilder(BASE_URL +"ts"+ removingDepartmentId).delete();
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldThrowMethodNotAllowedErrorDeletingDepartmentWithInvalidHttpMethod() {
        Response response = resourceTestHelper.requestBuilder(BASE_URL).delete();
        assertMethodNotAllowedResponse(response);
    }


    @Test
    public void shouldUpdateDepartment() throws DepartmentNotFound {
        Integer actualDepartmentId = 1;
        Department updatedDepartment  = new Department( 1,"freshworks");
        when(DEPARTMENT_SERVICE.updateDepartmentById(actualDepartmentId, updatedDepartment)).thenReturn(updatedDepartment);
        Response response = resourceTestHelper.requestBuilder(BASE_URL + actualDepartmentId)
                                              .put(Entity.json(updatedDepartment));
        assertOkResponse(response);
    }

    @Test
    public void shouldThrowDepartmentFoundErrorWhileUpdatingANonExistingDepartment(){
        int actualDepartmentId = 123;
        updatedDepartmentAsJsonString = "{\"name\":\"freshworks\"}";
        Response response = resourceTestHelper.requestBuilder(BASE_URL + "ts" + actualDepartmentId)
                .put(Entity.json(updatedDepartmentAsJsonString));
        assertNotFoundResponse(response);
    }

    @Test
    public void shouldThrowUnprocessibleEntityErrorWhileUpdatingDepartmentWithEmptyFields() {
        updatedDepartmentAsJsonString = "{}";
        int actualDepartmentId = 1;
        Response response = resourceTestHelper.requestBuilder(BASE_URL + actualDepartmentId)
                .put(Entity.json(updatedDepartmentAsJsonString));
        assertUnprocessibleEntityResponse(response);
    }

    @Test
    public void shouldThrowMethodNotAllowedErrorUpdatingDepartmentWithInvalidHttpMethod() {
        inputDepartmentAsJsonString = "{\"name\":\"freshworks\"}";
        Response response =  resourceTestHelper.requestBuilder(BASE_URL).put(Entity.json(inputDepartmentAsJsonString));
        assertMethodNotAllowedResponse(response);
    }


    @Override
    protected ResourceTestRule getResourceTestRule(){
        return RESOURCE_TEST_RULE;
    }

    @Override
    protected void populateStubData(){
        super.populateStubData();
        BASE_URL = "http://localhost:8080/departments/";
    }

    @Override
    protected DepartmentService serviceMock(){
        return DEPARTMENT_SERVICE;
    }
}
