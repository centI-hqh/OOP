import java.util.*;
import java.util.stream.Collectors;

public class ResourceManager<T extends Upgradeable> {
    private Map<String, T> resources;
    private List<String> resourceTypes;
    private int totalUpgradeCost;
    private int availableGold;

    public ResourceManager(int initialGold) {
        this.resources = new TreeMap<>();
        this.resourceTypes = new ArrayList<>();
        this.totalUpgradeCost = 0;
        this.availableGold = initialGold;
    }

    public void addResource(String name, T resource) {
        resources.put(name, resource);
        String type = resource.getClass().getSimpleName();
        if (!resourceTypes.contains(type)) {
            resourceTypes.add(type);
        }
        updateTotalUpgradeCost();
    }

    public T getResource(String name) {
        return resources.get(name);
    }

    public boolean upgradeResource(String name) {
        T resource = resources.get(name);
        if (resource != null && resource.canUpgrade() && availableGold >= resource.getUpgradeCost()) {
            if (resource.upgrade()) {
                availableGold -= resource.getUpgradeCost();
                updateTotalUpgradeCost();
                return true;
            }
        }
        return false;
    }

    public List<T> getUpgradeableResources() {
        return resources.values().stream()
                .filter(Upgradeable::canUpgrade)
                .collect(Collectors.toList());
    }

    public List<T> getResourcesByType(String type) {
        return resources.values().stream()
                .filter(r -> r.getClass().getSimpleName().equals(type))
                .collect(Collectors.toList());
    }

    public List<T> getResourcesByLevel(int minLevel) {
        return resources.values().stream()
                .filter(r -> r.getLevel() >= minLevel)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getUpgradeCostSummary() {
        return resources.values().stream()
                .filter(Upgradeable::canUpgrade)
                .collect(Collectors.groupingBy(
                        r -> r.getClass().getSimpleName(),
                        Collectors.summingInt(Upgradeable::getUpgradeCost)
                ));
    }

    public Map<String, Double> getAverageLevelSummary() {
        return resources.values().stream()
                .collect(Collectors.groupingBy(
                        r -> r.getClass().getSimpleName(),
                        Collectors.averagingInt(Upgradeable::getLevel)
                ));
    }

    private void updateTotalUpgradeCost() {
        totalUpgradeCost = resources.values().stream()
                .filter(Upgradeable::canUpgrade)
                .mapToInt(Upgradeable::getUpgradeCost)
                .sum();
    }

    public void addGold(int amount) {
        availableGold += amount;
    }

    public boolean spendGold(int amount) {
        if (availableGold >= amount) {
            availableGold -= amount;
            return true;
        }
        return false;
    }

    public int getTotalUpgradeCost() { return totalUpgradeCost; }
    public int getAvailableGold() { return availableGold; }
    public int getResourceCount() { return resources.size(); }
    public List<String> getResourceTypes() { return new ArrayList<>(resourceTypes); }

    @Override
    public String toString() {
        return String.format("ResourceManager[Gold: %d, Resources: %d, UpgradeCost: %d]",
                availableGold, resources.size(), totalUpgradeCost);
    }
}