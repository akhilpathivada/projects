package service;

import ems.service.DepartmentService;
import ems.util.TransactionManager;
import ems.util.TransactionWrapper;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DepartmentServiceTest {
    private  DepartmentService DEPARTMENT_SERVICE;
    private TransactionWrapper transactionWrapper;
    @Before
    public void before(){
        DEPARTMENT_SERVICE = mock(DepartmentService.class);
        transactionWrapper = new TransactionWrapper();
        TransactionManager transactionManager = transactionWrapper.createTransactionManager(DEPARTMENT_SERVICE.getSession());
    }

    @Test
    public void create(){

    }
}