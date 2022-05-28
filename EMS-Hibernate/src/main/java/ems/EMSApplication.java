package ems;

import ems.model.Department;
import ems.model.Employee;
import ems.resource.DepartmentResource;
import ems.resource.EmployeeResource;
import ems.service.DepartmentService;
import ems.service.EmployeeService;
import ems.util.TransactionWrapper;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EMSApplication extends Application<EMSConfiguration> {

    public static void main(String[] args) throws Exception {
        new EMSApplication().run(args);
    }
    private final HibernateBundle<EMSConfiguration> hibernate =
            new HibernateBundle<EMSConfiguration>(Department.class, Employee.class) {

        @Override
        public DataSourceFactory getDataSourceFactory(EMSConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<EMSConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(EMSConfiguration configuration, Environment environment) {
        final EmployeeService employeeService = new EmployeeService(hibernate.getSessionFactory(), new TransactionWrapper());
        final DepartmentService departmentService = new DepartmentService(hibernate.getSessionFactory(), employeeService, new TransactionWrapper());
        final EmployeeResource employeeResource = new EmployeeResource(employeeService, departmentService);
        final DepartmentResource departmentResource = new DepartmentResource(departmentService);
        environment.jersey().register(employeeResource);
        environment.jersey().register(departmentResource);
    }
}