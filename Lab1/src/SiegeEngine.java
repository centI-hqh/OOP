class SiegeEngine {
    private String name;
    private Position position;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int movementRange;

    public SiegeEngine(String name, Position position, int health, int attack, int defense, int movementRange) {
        this.name = name;
        this.position = position;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.movementRange = movementRange;
    }

    public String getName() { return name; }
    public Position getPosition() { return position; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getMovementRange() { return movementRange; }

    public boolean moveTo(Position newPosition) {
        double distance = position.distanceTo(newPosition);
        if (distance <= movementRange) {
            position = newPosition;
            return true;
        }
        return false;
    }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health = Math.max(0, health - actualDamage);
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public String toString() {
        return name + " [" + health + "/" + maxHealth + " HP] " + position;
    }
}