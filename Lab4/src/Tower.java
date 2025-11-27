public class Tower extends CastleElement implements Producible {
    private int arrowCapacity;
    private int currentArrows;

    public Tower(String name, int health, int defense, int arrowCapacity) {
        super(name, health, defense);
        this.arrowCapacity = arrowCapacity;
        this.currentArrows = arrowCapacity;
    }

    // Combatable методы
    @Override
    public int attack(Combatable target) {
        if (isDestroyed || currentArrows <= 0) return 0;

        currentArrows--;
        int damage = 20 + level * 5;
        target.takeDamage(damage);
        System.out.println(name + " выпускает стрелу! Урон: " + damage);
        return damage;
    }

    @Override
    public boolean canAttack(Combatable target) {
        return !isDestroyed && currentArrows > 0;
    }

    @Override
    public int getAttack() {
        return 20 + level * 5;
    }

    // Upgradeable методы
    @Override
    public boolean upgrade() {
        if (!canUpgrade()) return false;

        level++;
        maxHealth += 100;
        health = maxHealth;
        defense += 5;
        arrowCapacity += 20;
        currentArrows = arrowCapacity;
        System.out.println(name + " улучшена до уровня " + level + "!");
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canUpgrade() {
        return !isDestroyed && level < getMaxLevel();
    }

    @Override
    public int getUpgradeCost() {
        return level * 100;
    }

    @Override
    public String getLevelBonuses() {
        return "+100 HP, +5 защита, +20 стрел";
    }

    // Producible методы
    @Override
    public int produce() {
        if (!canProduce()) return 0;

        int production = getProductionRate();
        currentArrows = Math.min(arrowCapacity, currentArrows + production);
        System.out.println(name + " производит " + production + " стрел");
        return production;
    }

    @Override
    public int getProductionRate() {
        return 5 + level * 2;
    }

    @Override
    public boolean canProduce() {
        return !isDestroyed && currentArrows < arrowCapacity;
    }

    @Override
    public int getProductionCost() {
        return 10;
    }

    @Override
    public String getProductType() {
        return "Стрелы";
    }

    public void resupplyArrows(int amount) {
        currentArrows = Math.min(arrowCapacity, currentArrows + amount);
        System.out.println(name + " пополнена: " + currentArrows + "/" + arrowCapacity + " стрел");
    }
}