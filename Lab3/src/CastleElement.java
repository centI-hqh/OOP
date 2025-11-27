public abstract class CastleElement implements Combatable, Upgradeable, Repairable, Comparable<CastleElement>, Cloneable, java.io.Serializable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int level;
    protected int maxLevel;
    protected boolean isDestroyed;

    public CastleElement(String name, int health, int attack, int defense) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.level = 1;
        this.maxLevel = 3;
        this.isDestroyed = false;
    }

    // Реализация Combatable
    @Override
    public int attack(Combatable target) {
        if (canAttack(target)) {
            int damage = calculateDamage();
            target.takeDamage(damage);
            return damage;
        }
        return 0;
    }

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
    public boolean canAttack(Combatable target) {
        return !isDestroyed && target.isAlive();
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public boolean isAlive() {
        return !isDestroyed;
    }

    // Реализация Upgradeable
    @Override
    public boolean upgrade() {
        if (!canUpgrade()) return false;

        level++;
        maxHealth += 50;
        health = maxHealth;
        attack += 10;
        defense += 5;
        System.out.println(name + " улучшен до уровня " + level + "!");
        return true;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean canUpgrade() {
        return !isDestroyed && level < maxLevel;
    }

    @Override
    public int getUpgradeCost() {
        return level * 100;
    }

    @Override
    public String getLevelBonuses() {
        return "+50 HP, +10 ATK, +5 DEF";
    }

    // Реализация Repairable
    @Override
    public boolean repair(int amount) {
        if (!canBeRepaired()) return false;

        health = Math.min(maxHealth, health + amount);
        System.out.println(name + " отремонтирован на " + amount + " HP");
        return true;
    }

    @Override
    public boolean canBeRepaired() {
        return !isDestroyed && health < maxHealth;
    }

    @Override
    public int getRepairCost() {
        return (maxHealth - health) * 2;
    }

    // Реализация Comparable
    @Override
    public int compareTo(CastleElement other) {
        return Integer.compare(this.health, other.health);
    }

    // Реализация Cloneable
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Общие методы
    protected int calculateDamage() {
        return attack + (int)(Math.random() * 5);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public String toString() {
        return name + " [Ур." + level + "] [" + health + "/" + maxHealth + " HP] " +
                "Атака: " + attack + " Защита: " + defense;
    }
}