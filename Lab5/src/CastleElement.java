public abstract class CastleElement implements Combatable, Upgradeable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int defense;
    protected int level;
    protected boolean isDestroyed;
    protected final GameLogger logger;

    public CastleElement(String name, int health, int defense, GameLogger logger) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.defense = defense;
        this.level = 1;
        this.isDestroyed = false;
        this.logger = logger;
    }

    @Override
    public abstract int attack(Combatable target);

    @Override
    public void takeDamage(int damage) {
        try {
            if (isDestroyed) throw new CastleException(name, "Нельзя нанести урон разрушенному элементу");

            int actualDamage = Math.max(1, damage - defense);
            health -= actualDamage;
            logger.logBattleAction("ПОЛУЧЕНИЕ УРОНА", name,
                    String.format("урон: %d, здоровье: %d/%d", actualDamage, health, maxHealth));

            if (health <= 0) {
                isDestroyed = true;
                logger.logBattleAction("РАЗРУШЕНИЕ", name, "элемент замка разрушен!");
            }
        } catch (CastleException e) {
            logger.logException(e);
        }
    }

    @Override
    public abstract boolean canAttack(Combatable target);

    @Override
    public abstract int getAttack();

    @Override
    public int getDefense() { return defense; }

    @Override
    public boolean isAlive() { return !isDestroyed; }

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

    public int getHealth() { return health; }
    public boolean isDestroyed() { return isDestroyed; }

    public void repair(int amount) {
        try {
            if (isDestroyed) throw new CastleException(name, "Нельзя ремонтировать разрушенный элемент");
            health = Math.min(maxHealth, health + amount);
            logger.logInfo(name + " отремонтирован на " + amount + " HP");
        } catch (CastleException e) {
            logger.logException(e);
        }
    }

    @Override
    public String toString() {
        return name + " [Ур." + level + "] [" + health + "/" + maxHealth + " HP] Защита: " + defense +
                (isDestroyed ? " [РАЗРУШЕН]" : " [ЦЕЛ]");
    }
}