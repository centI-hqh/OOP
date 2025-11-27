import java.util.*;

public class SiegeEquipmentManager {
    private HashSet<SiegeEquipment> siegeEquipment;
    private HashMap<String, SiegeEquipment> equipmentByName;

    public SiegeEquipmentManager() {
        this.siegeEquipment = new HashSet<>();
        this.equipmentByName = new HashMap<>();
    }

    public void addEquipment(SiegeEquipment equipment) {
        if (siegeEquipment.add(equipment)) {
            equipmentByName.put(equipment.getName(), equipment);
        }
    }

    public boolean removeEquipment(SiegeEquipment equipment) {
        if (siegeEquipment.remove(equipment)) {
            equipmentByName.remove(equipment.getName());
            return true;
        }
        return false;
    }

    public SiegeEquipment getEquipmentByName(String name) {
        return equipmentByName.get(name);
    }

    public Set<SiegeEquipment> getOperationalEquipment() {
        Set<SiegeEquipment> operational = new HashSet<>();
        for (SiegeEquipment equipment : siegeEquipment) {
            if (equipment.isOperational) {
                operational.add(equipment);
            }
        }
        return operational;
    }

    public Set<SiegeEquipment> getEquipmentByType(Class<?> type) {
        Set<SiegeEquipment> result = new HashSet<>();
        for (SiegeEquipment equipment : siegeEquipment) {
            if (type.isInstance(equipment)) {
                result.add(equipment);
            }
        }
        return result;
    }

    public boolean containsEquipment(SiegeEquipment equipment) {
        return siegeEquipment.contains(equipment);
    }

    public int countOperationalEquipment() {
        int count = 0;
        for (SiegeEquipment equipment : siegeEquipment) {
            if (equipment.isOperational) {
                count++;
            }
        }
        return count;
    }

    public double getOperationalPercentage() {
        if (siegeEquipment.isEmpty()) return 0.0;
        return (double) countOperationalEquipment() / siegeEquipment.size() * 100;
    }

    @Override
    public String toString() {
        return String.format("SiegeEquipmentManager[%d equipment, %d operational (%.1f%%)]",
                siegeEquipment.size(), countOperationalEquipment(), getOperationalPercentage());
    }
}