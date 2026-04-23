
package exceptions;
/**
 *
 * @author Aruna Indika
 */
public class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
    
    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}