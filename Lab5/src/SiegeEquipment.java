public abstract class SiegeEquipment implements Combatable, Upgradeable, Producible {
    protected String name;
    protected int damage;
    protected int range;
    protected int ammunition;
    protected boolean isOperational;
    protected int level;
    protected final GameLogger logger;

    public SiegeEquipment(String name, int damage, int range, int ammunition, GameLogger logger) {
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.ammunition = ammunition;
        this.isOperational = true;
        this.level = 1;
        this.logger = logger;
    }

    @Override
    public abstract int attack(Combatable target);

    @Override
    public void takeDamage(int damage) {
        try {
            if (!isOperational) throw new SiegeException(name, "получение урона", "Орудие уже неисправно");

            if (Math.random() < 0.3) {
                isOperational = false;
                logger.logBattleAction("ПОЛОМКА", name, "осадное орудие сломано от урона!");
                throw new SiegeException(name, "получение урона", "Орудие сломалось от полученного урона");
            }
            logger.logBattleAction("УСТОЙЧИВОСТЬ", name, "орудие выдержало урон");
        } catch (SiegeException e) {
            logger.logException(e);
        }
    }

    @Override
    public abstract boolean canAttack(Combatable target);

    @Override
    public abstract int getAttack();

    @Override
    public int getDefense() { return 5; }

    @Override
    public boolean isAlive() { return isOperational; }

    @Override
    public String getName() { return name; }

    @Override
    public abstract boolean upgrade();

    @Override
    public int getLevel() { return level; }

    @Override
    public abstract int getMaxLevel();

    @Override
    public abstract boolean canUpgrade();

    @Override
    public abstract int getUpgradeCost();

    @Override
    public abstract String getLevelBonuses();

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

    public void repair() {
        isOperational = true;
        logger.logInfo(name + " отремонтирован");
    }

    @Override
    public String toString() {
        return name + " [Ур." + level + "] Урон: " + damage + " Дальность: " + range +
                " Боеприпасы: " + ammunition + (isOperational ? " [ИСПРАВЕН]" : " [СЛОМАН]");
    }
}