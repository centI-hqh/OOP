public class CastleException extends GameException {
  private final String castleElementName;

  public CastleException(String elementName, String message) {
    super("Ошибка замкового элемента " + elementName + ": " + message, "CASTLE_ERROR");
    this.castleElementName = elementName;
  }

  public CastleException(String elementName, String message, Throwable cause) {
    super("Ошибка замкового элемента " + elementName + ": " + message, cause, "CASTLE_ERROR");
    this.castleElementName = elementName;
  }

  public String getCastleElementName() { return castleElementName; }
}