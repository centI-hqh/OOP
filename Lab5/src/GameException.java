import java.time.LocalDateTime;

public class GameException extends Exception {
  private final String errorCode;
  private final LocalDateTime timestamp;

  public GameException(String message) {
    super(message);
    this.errorCode = "GAME_ERROR";
    this.timestamp = LocalDateTime.now();
  }

  public GameException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
    this.timestamp = LocalDateTime.now();
  }

  public GameException(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = "GAME_ERROR";
    this.timestamp = LocalDateTime.now();
  }

  public GameException(String message, Throwable cause, String errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
    this.timestamp = LocalDateTime.now();
  }

  public String getErrorCode() { return errorCode; }
  public LocalDateTime getTimestamp() { return timestamp; }

  @Override
  public String toString() {
    return String.format("[%s] %s: %s", errorCode, timestamp, getMessage());
  }
}