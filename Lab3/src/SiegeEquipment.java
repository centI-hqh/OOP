public abstract class SiegeEquipment implements Combatable, Producible, Repairable, Comparable<SiegeEquipment>, Cloneable, java.io.Serializable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int ammunition;
    protected boolean isOperational;

    public SiegeEquipment(String name, int health, int attack, int defense, int ammunition) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.ammunition = ammunition;
        this.isOperational = true;
    }

    // Реализация Combatable
    @Override
    public int attack(Combatable target) {
        if (canAttack(target)) {
            ammunition--;
            int damage = calculateDamage();
            target.takeDamage(damage);
            System.out.println(name + " атакует с уроном " + damage);
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
            isOperational = false;
            System.out.println(name + " уничтожен!");
        }
    }

    @Override
    public boolean canAttack(Combatable target) {
        return isOperational && ammunition > 0 && target.isAlive();
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
        return isOperational;
    }

    // Реализация Producible
    @Override
    public int produce() {
        if (canProduce()) {
            ammunition += getProductionRate();
            System.out.println(name + " производит " + getProductionRate() + " " + getProductType());
            return getProductionRate();
        }
        return 0;
    }

    @Override
    public int getProductionRate() {
        return 5;
    }

    @Override
    public boolean canProduce() {
        return isOperational;
    }

    @Override
    public int getProductionCost() {
        return 50;
    }

    @Override
    public String getProductType() {
        return "снаряды";
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
        return isOperational && health < maxHealth;
    }

    @Override
    public int getRepairCost() {
        return (maxHealth - health) * 3;
    }

    // Реализация Comparable
    @Override
    public int compareTo(SiegeEquipment other) {
        return Integer.compare(this.health, other.health);
    }

    // Реализация Cloneable
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    protected int calculateDamage() {
        return attack + (int)(Math.random() * 10);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAmmunition() {
        return ammunition;
    }

    @Override
    public String toString() {
        return name + " [" + health + "/" + maxHealth + " HP] " +
                "Атака: " + attack + " Снаряды: " + ammunition;
    }
}