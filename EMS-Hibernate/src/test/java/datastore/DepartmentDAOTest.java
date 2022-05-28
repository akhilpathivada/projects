package datastore;

import ems.datastore.DepartmentDAO;
import ems.service.DepartmentService;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.ClassRule;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

public class DepartmentDAOTest {
    @ClassRule
    private DepartmentService departmentService;
    private DepartmentDAO departmentDAO;
    private Session session;
    EntityManager entityManager

    @Before
    public void before(){
        departmentService = null;
        departmentDAO = new DepartmentDAO();
        session = departmentService.getSession();
    }

    @Autowired
}
