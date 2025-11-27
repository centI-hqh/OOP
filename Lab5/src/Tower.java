public class Tower extends CastleElement implements Producible {
    private int arrowCapacity;
    private int currentArrows;

    public Tower(String name, int health, int defense, int arrowCapacity, GameLogger logger) {
        super(name, health, defense, logger);
        this.arrowCapacity = arrowCapacity;
        this.currentArrows = arrowCapacity;
    }

    @Override
    public int attack(Combatable target) {
        try {
            if (isDestroyed || currentArrows <= 0) return 0;

            currentArrows--;
            int damage = 20 + level * 5;
            target.takeDamage(damage);
            logger.logBattleAction("ВЫСТРЕЛ ИЗ БАШНИ", name, "урон: " + damage + ", стрел осталось: " + currentArrows);
            return damage;
        } catch (Exception e) {
            logger.logError("Ошибка атаки башни", e);
            return 0;
        }
    }

    @Override
    public boolean canAttack(Combatable target) {
        return !isDestroyed && currentArrows > 0 && target != null;
    }

    @Override
    public int getAttack() { return 20 + level * 5; }

    @Override
    public boolean upgrade() {
        try {
            if (!canUpgrade()) throw new CastleException(name, "Невозможно улучшить башню");

            level++;
            maxHealth += 100;
            health = maxHealth;
            defense += 5;
            arrowCapacity += 20;
            currentArrows = arrowCapacity;
            logger.logInfo(name + " улучшена до уровня " + level + "!");
            return true;
        } catch (CastleException e) {
            logger.logException(e);
            return false;
        }
    }

    @Override
    public int getMaxLevel() { return 3; }

    @Override
    public boolean canUpgrade() { return !isDestroyed && level < getMaxLevel(); }

    @Override
    public int getUpgradeCost() { return level * 100; }

    @Override
    public String getLevelBonuses() { return "+100 HP, +5 защита, +20 стрел"; }

    @Override
    public int produce() {
        try {
            if (!canProduce()) return 0;

            int production = getProductionRate();
            currentArrows = Math.min(arrowCapacity, currentArrows + production);
            logger.logInfo(name + " производит " + production + " стрел");
            return production;
        } catch (Exception e) {
            logger.logError("Ошибка производства стрел", e);
            return 0;
        }
    }

    @Override
    public int getProductionRate() { return 5 + level * 2; }

    @Override
    public boolean canProduce() { return !isDestroyed && currentArrows < arrowCapacity; }

    @Override
    public int getProductionCost() { return 10; }

    @Override
    public String getProductType() { return "Стрелы"; }
}