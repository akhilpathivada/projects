package ems.datastore;

import ems.exceptions.EmployeeNotFound;
import ems.model.Employee;
import org.hibernate.Session;
import java.util.List;

public class EmployeeDAO {
    public Employee create(Session session, Employee employee){
        session.save(employee);
        return employee;
    }

    public Employee getEmployeeRecord(Session session, int EmployeeId){
        return session.get(Employee.class, EmployeeId);
    }

    public void delete(Session session, int empId) throws EmployeeNotFound {
        session.delete(getEmployeeRecord(session, empId));
    }

    public void update(Session session, Employee employee){
        session.saveOrUpdate(employee);
    }

    public List<Employee> getEmployeesBasedOnDepartmentId(Session session, int depId){
        return session.createQuery("FROM Employee emp WHERE depId =:departmentId")
                .setParameter("departmentId", depId).list();
    }

    public void deleteEmployeesBasedOnDepartmentId(Session session, int depId){
        session.createQuery("DELETE FROM Employee e WHERE e.depId=:departmentId")
                .setParameter("departmentId", depId).executeUpdate();
    }

    public List<Employee> sortEmployeesBasedOnName(Session session){
        return session.createQuery("FROM Employee f ORDER BY f.name").list();
    }

    public boolean isEmployeeRecordExists(Session session, int empId){
        return (Long)session.createQuery( "select count(*) from Employee e where e.empId=:targetEmployeeId")
                            .setParameter("targetEmployeeId", empId)
                            .uniqueResult() > 0;
    }
}
