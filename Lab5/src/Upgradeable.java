public interface Upgradeable {
    boolean upgrade();
    int getLevel();
    int getMaxLevel();
    boolean canUpgrade();
    int getUpgradeCost();
    String getLevelBonuses();
}