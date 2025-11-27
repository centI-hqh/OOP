public class Catapult extends SiegeEquipment {
    private boolean isLoaded;

    public Catapult(String name, int damage, int range, int ammunition, GameLogger logger) {
        super(name, damage, range, ammunition, logger);
        this.isLoaded = false;
    }

    @Override
    public int attack(Combatable target) {
        try {
            if (!isOperational || !isLoaded || ammunition <= 0) return 0;

            ammunition--;
            isLoaded = false;
            logger.logBattleAction("ЗАПУСК СНАРЯДА", name, "урон: " + damage + ", снарядов: " + ammunition);
            target.takeDamage(damage);
            return damage;
        } catch (Exception e) {
            logger.logError("Ошибка атаки катапульты", e);
            return 0;
        }
    }

    @Override
    public boolean canAttack(Combatable target) {
        return isOperational && isLoaded && ammunition > 0;
    }

    @Override
    public int getAttack() { return damage; }

    @Override
    public boolean upgrade() {
        try {
            if (!canUpgrade()) throw new SiegeException(name, "улучшение", "Невозможно улучшить катапульту");

            level++;
            damage += 15;
            range += 10;
            logger.logInfo(name + " улучшена до уровня " + level + "!");
            return true;
        } catch (SiegeException e) {
            logger.logException(e);
            return false;
        }
    }

    @Override
    public int getMaxLevel() { return 3; }

    @Override
    public boolean canUpgrade() { return isOperational && level < getMaxLevel(); }

    @Override
    public int getUpgradeCost() { return level * 150; }

    @Override
    public String getLevelBonuses() { return "+15 урона, +10 дальности"; }

    @Override
    public int produce() {
        try {
            if (!canProduce()) return 0;

            int production = getProductionRate();
            ammunition += production;
            logger.logInfo(name + " производит " + production + " снарядов");
            return production;
        } catch (Exception e) {
            logger.logError("Ошибка производства снарядов", e);
            return 0;
        }
    }

    @Override
    public int getProductionRate() { return 2 + level; }

    @Override
    public boolean canProduce() { return isOperational; }

    @Override
    public int getProductionCost() { return 20; }

    @Override
    public String getProductType() { return "Снаряды"; }

    public void reload() {
        if (ammunition > 0 && !isLoaded) {
            isLoaded = true;
            logger.logInfo(name + " заряжена");
        }
    }
}