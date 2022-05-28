package datastore;

import ems.util.TransactionManager;
import ems.util.TransactionWrapper;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExternalResource;

/**
 * Created by sesha on 06/03/17.
 */
public abstract class BaseDAOTest {
    protected TransactionWrapper transactionWrapper = new TransactionWrapper();
    private Session currentSession;
    @Rule
    public final ExternalResource transactionWrapperRule = new ExternalResource() {
        private TransactionManager transactionManager;

        @Override
        protected void before() throws Throwable {
         //   DataStoreUtil.buildSessionFactory(TestUtil.getHibernateConfigPath());
           // transactionManager = transactionWrapper.createTransactionManager();
            currentSession = transactionManager.getCurrentSession();
        }

        @Override
        protected void after() {
            transactionManager.rollback();
            transactionManager.close();
        }
    };

    protected Session getCurrentSession() {
        return currentSession;
    }

    @Before
    public void before() {
        populateDependencies(currentSession);
    }

    protected abstract void populateDependencies(Session session);
}
