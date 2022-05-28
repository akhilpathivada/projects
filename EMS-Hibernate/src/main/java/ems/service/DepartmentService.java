package ems.service;

import ems.datastore.DepartmentDAO;
import ems.exceptions.DepartmentNotFound;
import ems.model.Department;
import ems.model.Employee;
import ems.util.TransactionManager;
import ems.util.TransactionWrapper;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.List;

public class DepartmentService extends AbstractDAO<Department> {
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private EmployeeService employeeService;
    private TransactionWrapper transactionWrapper;
    private Session session;

    public DepartmentService(SessionFactory factory, EmployeeService employeeService, TransactionWrapper transactionWrapper) {
        super(factory);
        this.employeeService = employeeService;
        this.transactionWrapper = transactionWrapper;
       // this.session = currentSession();
    }

    public Session getSession(){
        return currentSession();
    }

    public List<Department> getAllDepartments() {
        return (List<Department>) currentSession().createCriteria(Department.class).list();
    }

    public Department getDepartmentById(int depId) throws DepartmentNotFound {
        Department expectedDepartment = null;
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
            if (isDepartmentExists(depId)) {
                expectedDepartment = departmentDAO.getDepartmentRecord(transactionManager.getCurrentSession(), depId);
                transactionManager.commit();
            }
        }
        catch(DepartmentNotFound e){
            transactionManager.rollback();
            throw e;
        }
        return expectedDepartment;
    }

    public List<Employee> getEmployeesOfDepartment(int depId) throws DepartmentNotFound {
        List<Employee> employeeList = null;
        try {
            if (isDepartmentExists(depId)) {
                employeeList = employeeService.getEmployeesOnDepartmentId(depId);
            }
        }
        catch (DepartmentNotFound e) {
            throw e;
        }
        return employeeList;
    }

    public Department createDepartment(Department department) throws SQLException {
        department.setDepId(getLastInsertedRecordId() + 1);

        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
                departmentDAO.create(transactionManager.getCurrentSession(), department);
                transactionManager.commit();
                return department;
        }
        catch (SQLException e) {
            transactionManager.rollback();
            throw e;
        }
        catch (PersistenceException e) {
            transactionManager.rollback();
            throw e;
        }
    }

    public void deleteDepartment(int depId) throws DepartmentNotFound {
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
            if (isDepartmentExists(depId)){
                departmentDAO.delete(transactionManager.getCurrentSession(), depId);
                employeeService.deleteAllEmployeesOfDepartment(depId);
                transactionManager.commit();
            }
        }
        catch (DepartmentNotFound e) {
            transactionManager.rollback();
            throw e;
        }
    }

    public Department updateDepartmentById(Integer depId, Department department) throws DepartmentNotFound{
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(currentSession());
        try {
            if (isDepartmentExists(depId)){
                departmentDAO.update(transactionManager.getCurrentSession(), department);
                transactionManager.commit();
            }
        }
        catch (DepartmentNotFound e) {
            transactionManager.rollback();
            throw e;
        }
        return department;
    }

    public boolean isDepartmentExists(int depId) throws DepartmentNotFound {
        String queryString = "select count(*) from Department d where d.depId=:targetDepartmentId";
        boolean exists =  (Long)currentSession().createQuery(queryString)
                                                .setParameter("targetDepartmentId", depId)
                                                .uniqueResult() > 0;
        if(!exists){
            throw new DepartmentNotFound();
        }
        return true;
    }

    private int getLastInsertedRecordId(){
        int table_size = ((Long)currentSession().createQuery("select count(*) from Department")
                                                .uniqueResult())
                                                .intValue();
        if(table_size == 0)
            return table_size;
        return (int) currentSession().createQuery("Select max(depId) FROM Department dep").list().get(0);
    }
}