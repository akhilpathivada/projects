package ems.exceptions;

import javax.persistence.PersistenceException;

public class InvalidDataException  {
    //private ErrorCode errorCode;

    public InvalidDataException(PersistenceException cause) {

        /*if (cause instanceof ConstraintViolationException && ((ConstraintViolationException) cause).getSQLException().getMessage().contains("Duplicate entry")) {
            errorCode = ErrorCode.DuplicateValue;
        }
        else {
            errorCode = ErrorCode.InvalidValue;
        }
*/
    }


}

