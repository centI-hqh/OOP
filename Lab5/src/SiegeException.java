public class SiegeException extends GameException {
    private final String siegeEquipmentName;
    private final String action;

    public SiegeException(String equipmentName, String action, String message) {
        super("Ошибка осадного орудия " + equipmentName + " при " + action + ": " + message,
                "SIEGE_ERROR");
        this.siegeEquipmentName = equipmentName;
        this.action = action;
    }

    public SiegeException(String equipmentName, String action, String message, Throwable cause) {
        super("Ошибка осадного орудия " + equipmentName + " при " + action + ": " + message,
                cause, "SIEGE_ERROR");
        this.siegeEquipmentName = equipmentName;
        this.action = action;
    }

    public String getSiegeEquipmentName() { return siegeEquipmentName; }
    public String getAction() { return action; }
}