package ems.resource;

import ems.exceptions.DepartmentNotFound;
import ems.exceptions.EmployeeNotFound;
import ems.model.ApiStatus;
import ems.model.Employee;
import ems.service.DepartmentService;
import ems.service.EmployeeService;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/employees")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class EmployeeResource {

    private EmployeeService employeeService;

    public EmployeeResource(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        employeeService.createDepartmentServiceObject(departmentService);
    }

    @GET
    @UnitOfWork
    public Response getAllEmployees(@DefaultValue("") @QueryParam("value") String attribute ){
        try {
            return Response.ok(employeeService.getAllEmployees(attribute)).build();
        }
        catch (IllegalArgumentException e) {
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    "Unidentified Attribute"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                            "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getEmployeeById(@PathParam("id") Integer empId){
        try {
            return Response.ok(employeeService.getEmployeeById(empId)).build();
        }
        catch (EmployeeNotFound e) {
            StringBuilder message = new StringBuilder("Employee ");
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    message.append(empId).append(" not found").toString()))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e) {
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                    "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @UnitOfWork
    public Response createEmployee(@Valid Employee employee) {
        try {
            employeeService.createEmployee(employee);
            return Response.created(new URI("/")).entity(employee).build();
        }
        catch (DepartmentNotFound e){
            StringBuilder message = new StringBuilder("Department ");
            return  Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ApiStatus(Response.Status.BAD_REQUEST.getStatusCode(),
                                    message.append(employee.getDepId()).append(" not exists").toString()))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                    "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Response updateEmployeeById(@PathParam("id") Integer empId, @Valid Employee employee) {
        try {
            return Response.ok(employeeService.updateEmployee(empId, employee)).build();
        }
        catch (DepartmentNotFound e){
            StringBuilder message = new StringBuilder("Department ");
            return  Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ApiStatus(Response.Status.BAD_REQUEST.getStatusCode(),
                                    message.append(employee.getDepId()).append(" not exists").toString()))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e) {
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                    "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteEmployeeById(@PathParam("id") Integer empId) {
        try{
            employeeService.deleteEmployee(empId);
            return Response.noContent().build();
        }
        catch (EmployeeNotFound e){
            StringBuilder message = new StringBuilder("Employee ");
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    message.append(empId).append(" not found").toString()))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e) {
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                    "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }
}