import java.util.*;
import java.util.stream.Collectors;

public class Inventory<T> {
    private Map<String, T> items;
    private Map<String, Integer> itemCount;
    private int maxCapacity;
    private int currentCapacity;

    public Inventory(int maxCapacity) {
        this.items = new HashMap<>();
        this.itemCount = new HashMap<>();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

    public boolean addItem(String name, T item) {
        if (currentCapacity < maxCapacity) {
            items.put(name, item);
            itemCount.put(name, itemCount.getOrDefault(name, 0) + 1);
            currentCapacity++;
            return true;
        }
        return false;
    }

    public T getItem(String name) {
        return items.get(name);
    }

    public T removeItem(String name) {
        T item = items.remove(name);
        if (item != null) {
            int count = itemCount.getOrDefault(name, 0);
            if (count > 1) {
                itemCount.put(name, count - 1);
            } else {
                itemCount.remove(name);
            }
            currentCapacity--;
        }
        return item;
    }

    public boolean containsItem(String name) {
        return items.containsKey(name);
    }

    public List<T> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public Set<String> getItemNames() {
        return new HashSet<>(items.keySet());
    }

    public Map<String, Integer> getItemSummary() {
        return new HashMap<>(itemCount);
    }

    public List<T> getItemsByType(Class<?> type) {
        return items.values().stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    public int getItemCount(String name) {
        return itemCount.getOrDefault(name, 0);
    }

    public int getCurrentCapacity() { return currentCapacity; }
    public int getMaxCapacity() { return maxCapacity; }
    public boolean isFull() { return currentCapacity >= maxCapacity; }
    public boolean isEmpty() { return currentCapacity == 0; }

    @Override
    public String toString() {
        return String.format("Inventory[%d/%d items: %s]",
                currentCapacity, maxCapacity, itemCount);
    }
}