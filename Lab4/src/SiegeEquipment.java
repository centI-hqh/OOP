public abstract class SiegeEquipment implements Combatable, Upgradeable, Producible {
    protected String name;
    protected int damage;
    protected int range;
    protected int ammunition;
    protected boolean isOperational;
    protected int level;

    public SiegeEquipment(String name, int damage, int range, int ammunition) {
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.ammunition = ammunition;
        this.isOperational = true;
        this.level = 1;
    }

    // Combatable методы
    @Override
    public abstract int attack(Combatable target);

    @Override
    public void takeDamage(int damage) {
        if (Math.random() < 0.3) {
            isOperational = false;
            System.out.println(name + " сломался от полученного урона!");
        }
    }

    @Override
    public abstract boolean canAttack(Combatable target);

    @Override
    public abstract int getAttack();

    @Override
    public int getDefense() {
        return 5;
    }

    @Override
    public boolean isAlive() {
        return isOperational;
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

    // Producible методы
    @Override
    public abstract int produce();

    @Override
    public abstract int getProductionRate();

    @Override
    public abstract boolean canProduce();

    @Override
    public abstract int getProductionCost();

    @Override
    public abstract String getProductType();

    // Общие методы
    public void repair() {
        isOperational = true;
        System.out.println(name + " отремонтирован");
    }

    @Override
    public String toString() {
        return name + " [Ур." + level + "] Урон: " + damage +
                " Дальность: " + range + " Боеприпасы: " + ammunition +
                " " + (isOperational ? "[ИСПРАВЕН]" : "[СЛОМАН]");
    }
}