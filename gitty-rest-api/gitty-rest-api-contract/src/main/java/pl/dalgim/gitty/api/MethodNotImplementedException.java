package pl.dalgim.gitty.api;

/**
 * Throwing when method not implemented.
 * @author Mateusz Dalgiewicz
 */
public class MethodNotImplementedException extends RuntimeException {

  public MethodNotImplementedException(String message) {
    super(message);
  }
}
