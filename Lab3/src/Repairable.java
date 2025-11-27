public interface Repairable {
    boolean repair(int amount);
    boolean canBeRepaired();
    int getRepairCost();
}