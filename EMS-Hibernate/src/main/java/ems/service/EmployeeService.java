package ems.service;

import ems.datastore.EmployeeDAO;
import ems.exceptions.DepartmentNotFound;
import ems.exceptions.EmployeeNotFound;
import ems.model.Employee;
import ems.util.TransactionManager;
import ems.util.TransactionWrapper;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService extends AbstractDAO<Employee> {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private DepartmentService departmentService;
    private TransactionWrapper transactionWrapper;

    public EmployeeService(SessionFactory factory, TransactionWrapper transactionWrapper) {
        super(factory);
        this.transactionWrapper = transactionWrapper;
    }

    public void createDepartmentServiceObject(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    public Employee createEmployee(Employee employee) throws DepartmentNotFound, SQLException {
        employee.setEmpId(getLastInsertedRecordId() + 1);

        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try{
            if(departmentService.isDepartmentExists(employee.getDepId())){
                employeeDAO.create(transactionManager.getCurrentSession(), employee);
                transactionManager.commit();
            }
        }
        catch(DepartmentNotFound e){
            transactionManager.rollback();
            throw e;
        }
        return persist(employee);
    }

    public List<Employee> getAllEmployees(String attribute) {
        if(attribute.isEmpty()) {
            return (List<Employee>) currentSession().createCriteria(Employee.class).list();
        }
        else if(attribute.equals("name")) {
            return sortEmployeesBasedOnName();
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public Employee getEmployeeById(Integer empId) throws EmployeeNotFound {
        Employee expectedEmployee = null;
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
            if (isEmployeeExists(empId)) {
                expectedEmployee = employeeDAO.getEmployeeRecord(transactionManager.getCurrentSession(), empId);
                transactionManager.commit();
            }
        }
        catch (EmployeeNotFound e){
            transactionManager.rollback();
            throw e;
        }
        return expectedEmployee;
    }

    public void deleteEmployee(int empId) throws EmployeeNotFound {
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
            if (isEmployeeExists(empId)) {
                employeeDAO.delete(transactionManager.getCurrentSession(), empId);
                transactionManager.commit();
            }
        }
        catch (EmployeeNotFound e){
            transactionManager.rollback();
            throw e;
        }
    }

    public Employee updateEmployee(Integer empId, Employee employee) throws Exception{
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try{
            if(isEmployeeExists(empId) && departmentService.isDepartmentExists(employee.getDepId())) {
                employee.setEmpId(empId);
                employeeDAO.update(currentSession(), employee);
                transactionManager.commit();
            }
        }
        catch(DepartmentNotFound e){
            transactionManager.rollback();
            throw e;
        }
        return employee;
    }

    public List<Employee> getEmployeesOnDepartmentId(Integer depId) throws DepartmentNotFound {
        List<Employee> employeeList = null;
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
            if (departmentService.isDepartmentExists(depId)){
                employeeList = employeeDAO.getEmployeesBasedOnDepartmentId(transactionManager.getCurrentSession(), depId);
                transactionManager.commit();
            }
        }
        catch (DepartmentNotFound e) {
            transactionManager.rollback();
            throw e;
        }
        return employeeList;
    }

    public void deleteAllEmployeesOfDepartment(Integer depId){
        try {
            employeeDAO.deleteEmployeesBasedOnDepartmentId(currentSession(), depId);
        }
        catch (Exception e){
            throw e;
        }
    }

    public List<Employee> sortEmployeesBasedOnName(){
        List<Employee> employeeList = null;
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try{
            employeeList = employeeDAO.sortEmployeesBasedOnName(transactionManager.getCurrentSession());
            transactionManager.commit();
        }
        catch (Exception e){
            transactionManager.rollback();
        }
        return employeeList;
    }

    private boolean isEmployeeExists(int empId) throws EmployeeNotFound {
        if(!employeeDAO.isEmployeeRecordExists(currentSession(), empId)){
            throw new EmployeeNotFound();
        }
        return true;
    }

    private int getLastInsertedRecordId(){
        int table_size = ((Long)currentSession().createQuery("select count(*) from Employee")
                                                .uniqueResult())
                                                .intValue();
        if(table_size == 0)
            return table_size;
        return (int) currentSession().createQuery("Select max(empId) FROM Employee emp").list().get(0);
    }
}
