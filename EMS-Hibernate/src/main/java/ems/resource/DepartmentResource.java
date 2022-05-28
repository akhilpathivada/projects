package ems.resource;

import ems.exceptions.DepartmentNotFound;
import ems.model.ApiStatus;
import ems.model.Department;
import ems.service.DepartmentService;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/departments")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DepartmentResource {
    private DepartmentService departmentService;

    public DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GET
    @UnitOfWork
    public Response getAllDepartments(){
        try {
            return Response.ok(departmentService.getAllDepartments()).build();
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
    public Response getDepartmentById(@PathParam("id") Integer depId){
        try {
            return Response.ok(departmentService.getDepartmentById(depId)).build();
        }
        catch (DepartmentNotFound e){
            StringBuilder message = new StringBuilder("Department ");
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    message.append(depId).append(" not found").toString()))
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
    @Path("/{depId}/employees")
    @UnitOfWork
    public Response getEmployeesOfDepartment(@PathParam("depId") Integer depId){
        try {
            return Response.ok(departmentService.getEmployeesOfDepartment(depId)).build();
        }
        catch (DepartmentNotFound e){
            StringBuilder message = new StringBuilder("Department ");
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    message.append(depId).append(" not found").toString()))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                    "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @UnitOfWork
    public Response createDepartment(@Valid Department department) {
        try {
            departmentService.createDepartment(department);
            return Response.created(new URI("/")).entity(department).build();
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
    public Response updateDepartmentById(@PathParam("id") Integer depId, @Valid Department department) {
        try {
            department = department.setDepId(depId);
            return Response.ok(departmentService.updateDepartmentById(depId, department)).entity(department).build();
        }
        catch (DepartmentNotFound e) {
            StringBuilder message = new StringBuilder("Department ");
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    message.append(depId).append(" not found").toString()))
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
    public Response deleteDepartmentById(@PathParam("id") Integer depId){

        try{
            departmentService.deleteDepartment(depId);
            return Response.noContent().build();
        }
        catch (DepartmentNotFound e){
            StringBuilder message = new StringBuilder("Department ");
            return  Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiStatus(Response.Status.NOT_FOUND.getStatusCode(),
                                    message.append(depId).append(" not found").toString()))
                            .type(MediaType.APPLICATION_JSON).build();
        }
        catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ApiStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                    "Internal Server Error"))
                            .type(MediaType.APPLICATION_JSON).build();
        }
    }
}