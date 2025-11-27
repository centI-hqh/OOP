public enum TerrainType {
    GRASS("Трава", 1, true, 1.0, "🟩"),
    FOREST("Лес", 2, true, 1.3, "🌲"),
    ROAD("Дорога", 1, true, 0.7, "🟫"),
    WATER("Вода", 3, false, 2.0, "💧"),
    WALL("Стена замка", 5, false, 3.0, "🧱"),
    GATE("Ворота", 2, true, 1.5, "🚪"),
    TOWER("Башня", 4, false, 2.5, "🏰"),
    SIEGE_RAMP("Осадная рампа", 1, true, 0.8, "🔼"),
    BATTLEFIELD("Поле боя", 1, true, 1.2, "⚔️"),
    FORTIFICATION("Укрепление", 3, true, 1.4, "🛡️");

    private final String name;
    private final int defenseBonus;
    private final boolean passable;
    private final double movementCost;
    private final String emoji;

    TerrainType(String name, int defenseBonus, boolean passable, double movementCost, String emoji) {
        this.name = name;
        this.defenseBonus = defenseBonus;
        this.passable = passable;
        this.movementCost = movementCost;
        this.emoji = emoji;
    }

    public String getName() { return name; }
    public int getDefenseBonus() { return defenseBonus; }
    public boolean isPassable() { return passable; }
    public double getMovementCost() { return movementCost; }
    public String getEmoji() { return emoji; }
}