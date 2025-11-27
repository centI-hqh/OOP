public class Wall extends CastleElement {
    private int height;

    public Wall(String name, int health, int defense, int height, GameLogger logger) {
        super(name, health, defense, logger);
        this.height = height;
    }

    @Override
    public int attack(Combatable target) { return 0; } // Стены не атакуют

    @Override
    public boolean canAttack(Combatable target) { return false; }

    @Override
    public int getAttack() { return 0; }

    @Override
    public boolean upgrade() {
        try {
            if (!canUpgrade()) throw new CastleException(name, "Невозможно улучшить стену");

            level++;
            maxHealth += 50;
            health = maxHealth;
            defense += 3;
            height += 2;
            logger.logInfo(name + " укреплена до уровня " + level + "!");
            return true;
        } catch (CastleException e) {
            logger.logException(e);
            return false;
        }
    }

    @Override
    public int getMaxLevel() { return 4; }

    @Override
    public boolean canUpgrade() { return !isDestroyed && level < getMaxLevel(); }

    @Override
    public int getUpgradeCost() { return level * 80; }

    @Override
    public String getLevelBonuses() { return "+50 HP, +3 защита, +2 высота"; }

    public int getHeight() { return height; }
}