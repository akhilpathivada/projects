package ems.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransactionManager implements AutoCloseable {
    protected Transaction transaction;
    private  Session session;

    public TransactionManager(Session session, Transaction transaction) {
        this.session = session;
        this.transaction = transaction;
    }

    public final Session getCurrentSession() {
        return session;
    }

    public void commit() {
        transaction.commit();
    }

    public void rollback() {
        transaction.rollback();
    }

    @Override
    public void close() {
        try {
            session.close();
        }
        catch (IllegalStateException | HibernateException exception) {

        }
    }
}

