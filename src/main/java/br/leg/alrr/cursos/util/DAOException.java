package br.leg.alrr.cursos.util;

/**
 *
 * @author Heliton
 */
public class DAOException extends Exception {

    public DAOException() {
    }

    /**
     * Construtor
     *
     * @param message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Construtor
     *
     * @param cause
     */
    public DAOException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor
     *
     * @param message
     * @param cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

}
