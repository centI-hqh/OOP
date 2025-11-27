public abstract class CastleElement implements Combatable, Upgradeable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int defense;
    protected int level;
    protected boolean isDestroyed;

    public CastleElement(String name, int health, int defense) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.defense = defense;
        this.level = 1;
        this.isDestroyed = false;
    }

    // Combatable методы
    @Override
    public abstract int attack(Combatable target);

    @Override
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        System.out.println(name + " получает урон " + actualDamage);

        if (health <= 0) {
            isDestroyed = true;
            System.out.println(name + " разрушен!");
        }
    }

    @Override
    public abstract boolean canAttack(Combatable target);

    @Override
    public abstract int getAttack();

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public boolean isAlive() {
        return !isDestroyed;
    }

    @Override
    public String getName() {
        return name;
    }

    // Upgradeable методы
    @Override
    public abstract boolean upgrade();

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public abstract int getMaxLevel();

    @Override
    public abstract boolean canUpgrade();

    @Override
    public abstract int getUpgradeCost();

    @Override
    public abstract String getLevelBonuses();

    // Общие методы
    public int getHealth() { return health; }
    public boolean isDestroyed() { return isDestroyed; }

    public void repair(int amount) {
        if (!isDestroyed) {
            health = Math.min(maxHealth, health + amount);
            System.out.println(name + " отремонтирован на " + amount + " HP");
        }
    }

    @Override
    public String toString() {
        return name + " [Ур." + level + "] [" + health + "/" + maxHealth + " HP] " +
                "Защита: " + defense + " " + (isDestroyed ? "[РАЗРУШЕН]" : "[ЦЕЛ]");
    }
}