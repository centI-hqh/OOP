public class DefenseException extends GameException {
    private final String defenderName;
    private final String defenseType;

    public DefenseException(String defenderName, String defenseType, String message) {
        super("Ошибка защитника " + defenderName + " (" + defenseType + "): " + message,
                "DEFENSE_ERROR");
        this.defenderName = defenderName;
        this.defenseType = defenseType;
    }

    public DefenseException(String defenderName, String defenseType, String message, Throwable cause) {
        super("Ошибка защитника " + defenderName + " (" + defenseType + "): " + message,
                cause, "DEFENSE_ERROR");
        this.defenderName = defenderName;
        this.defenseType = defenseType;
    }

    public String getDefenderName() { return defenderName; }
    public String getDefenseType() { return defenseType; }
}