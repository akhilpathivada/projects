package ems.datastore;

import ems.exceptions.DepartmentNotFound;
import ems.model.Department;
import org.hibernate.Session;

import java.sql.SQLException;

public class DepartmentDAO {

    public Department create(Session session, Department account) throws SQLException {
        session.save(account);
        return account;
    }

    public Department getDepartmentRecord(Session session, int departmentId) throws DepartmentNotFound {
        return session.get(Department.class, departmentId);
    }

    public void delete(Session session, int depId) throws DepartmentNotFound {
        session.delete(getDepartmentRecord(session, depId));
    }

    public void update(Session session, Department department){
        session.saveOrUpdate(department);
    }
}
