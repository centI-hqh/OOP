public class Catapult extends SiegeEquipment {
    private boolean isLoaded;

    public Catapult(String name, int damage, int range) {
        super(name, damage, range, 10);
        this.isLoaded = false;
    }

    // Combatable методы
    @Override
    public int attack(Combatable target) {
        if (!isOperational || !isLoaded || ammunition <= 0) return 0;

        ammunition--;
        isLoaded = false;
        System.out.println(name + " запускает снаряд! Урон: " + damage);
        target.takeDamage(damage);
        return damage;
    }

    @Override
    public boolean canAttack(Combatable target) {
        return isOperational && isLoaded && ammunition > 0;
    }

    @Override
    public int getAttack() {
        return damage;
    }

    // Upgradeable методы
    @Override
    public boolean upgrade() {
        if (!canUpgrade()) return false;

        level++;
        damage += 15;
        range += 10;
        System.out.println(name + " улучшена до уровня " + level + "!");
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canUpgrade() {
        return isOperational && level < getMaxLevel();
    }

    @Override
    public int getUpgradeCost() {
        return level * 150;
    }

    @Override
    public String getLevelBonuses() {
        return "+15 урона, +10 дальности";
    }

    // Producible методы
    @Override
    public int produce() {
        if (!canProduce()) return 0;

        int production = getProductionRate();
        ammunition += production;
        System.out.println(name + " производит " + production + " снарядов");
        return production;
    }

    @Override
    public int getProductionRate() {
        return 2 + level;
    }

    @Override
    public boolean canProduce() {
        return isOperational;
    }

    @Override
    public int getProductionCost() {
        return 20;
    }

    @Override
    public String getProductType() {
        return "Снаряды";
    }

    public void reload() {
        if (ammunition > 0 && !isLoaded) {
            isLoaded = true;
            System.out.println(name + " заряжена");
        }
    }
}