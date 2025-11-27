public class Defender implements Combatable, Upgradeable {
    private String name;
    private String type;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int level;
    private boolean isAlive;

    public Defender(String name, String type, int health, int attack, int defense) {
        this.name = name;
        this.type = type;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.level = 1;
        this.isAlive = true;
    }

    // Combatable методы
    @Override
    public int attack(Combatable target) {
        if (!isAlive) return 0;

        int damage = attack + (int)(Math.random() * 5);
        target.takeDamage(damage);
        System.out.println(name + " атакует! Урон: " + damage);
        return damage;
    }

    @Override
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        System.out.println(name + " получает урон " + actualDamage);

        if (health <= 0) {
            isAlive = false;
            System.out.println(name + " пал в бою!");
        }
    }

    @Override
    public boolean canAttack(Combatable target) {
        return isAlive;
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
        return isAlive;
    }

    @Override
    public String getName() {
        return name + " [" + type + "]";
    }

    // Upgradeable методы
    @Override
    public boolean upgrade() {
        if (!canUpgrade()) return false;

        level++;
        maxHealth += 20;
        health = maxHealth;
        attack += 5;
        defense += 3;
        System.out.println(name + " улучшен до уровня " + level + "!");
        return true;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canUpgrade() {
        return isAlive && level < getMaxLevel();
    }

    @Override
    public int getUpgradeCost() {
        return level * 50;
    }

    @Override
    public String getLevelBonuses() {
        return "+20 HP, +5 атака, +3 защита";
    }

    public void heal(int amount) {
        if (isAlive) {
            health = Math.min(maxHealth, health + amount);
            System.out.println(name + " исцелен на " + amount + " HP");
        }
    }

    @Override
    public String toString() {
        return name + " [" + type + "] Ур." + level + " [" + health + "/" + maxHealth + " HP] " +
                "Атака: " + attack + " Защита: " + defense + (isAlive ? " [ЖИВ]" : " [МЕРТВ]");
    }
}