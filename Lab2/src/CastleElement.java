public abstract class CastleElement {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int defense;
    protected boolean isDestroyed;

    public CastleElement(String name, int health, int defense) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.defense = defense;
        this.isDestroyed = false;
    }

    public abstract void defend();
    public abstract void takeDamage(int damage);
    public abstract String getStatus();

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isDestroyed() { return isDestroyed; }

    public void repair(int amount) {
        if (!isDestroyed) {
            health = Math.min(maxHealth, health + amount);
            System.out.println(name + " отремонтирован на " + amount + " HP");
        }
    }

    public double getHealthPercentage() {
        return (double) health / maxHealth * 100;
    }

    public String toString() {
        return name + " [" + health + "/" + maxHealth + " Здоровье] " +
                "Защита: " + defense + " " + getStatus();
    }
}