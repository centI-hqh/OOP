public class Wall extends CastleElement {
    private int height;

    public Wall(String name, int health, int defense, int height) {
        super(name, health, defense);
        this.height = height;
    }

    // Combatable методы
    @Override
    public int attack(Combatable target) {
        // Стены не атакуют
        return 0;
    }

    @Override
    public boolean canAttack(Combatable target) {
        return false;
    }

    @Override
    public int getAttack() {
        return 0;
    }

    // Upgradeable методы
    @Override
    public boolean upgrade() {
        if (!canUpgrade()) return false;

        level++;
        maxHealth += 50;
        health = maxHealth;
        defense += 3;
        height += 2;
        System.out.println(name + " укреплена до уровня " + level + "!");
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canUpgrade() {
        return !isDestroyed && level < getMaxLevel();
    }

    @Override
    public int getUpgradeCost() {
        return level * 80;
    }

    @Override
    public String getLevelBonuses() {
        return "+50 HP, +3 защита, +2 высота";
    }

    public int getHeight() {
        return height;
    }
}