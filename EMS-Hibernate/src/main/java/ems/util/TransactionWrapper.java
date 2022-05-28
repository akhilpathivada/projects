package ems.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransactionWrapper {

    public TransactionManager createTransactionManager(Session session) {
        Transaction transaction;
        transaction = session.beginTransaction();
        return new TransactionManager(session, transaction);
    }
}
