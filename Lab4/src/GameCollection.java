import java.util.*;
import java.util.stream.Collectors;

public class GameCollection<E extends Combatable> {
    private List<E> entities;
    private Map<String, E> entityByName;

    public GameCollection() {
        this.entities = new ArrayList<>();
        this.entityByName = new HashMap<>();
    }

    public void add(E entity) {
        entities.add(entity);
        entityByName.put(entity.getName(), entity);
    }

    public void remove(E entity) {
        entities.remove(entity);
        entityByName.remove(entity.getName());
    }

    public E getByName(String name) {
        return entityByName.get(name);
    }

    public List<E> getActiveEntities() {
        return entities.stream()
                .filter(Combatable::isAlive)
                .collect(Collectors.toList());
    }

    public List<E> getEntitiesByType(Class<?> type) {
        return entities.stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    public List<E> getStrongEntities(int attackThreshold) {
        return entities.stream()
                .filter(e -> e.getAttack() >= attackThreshold && e.isAlive())
                .collect(Collectors.toList());
    }

    public List<E> getWeakEntities() {
        return entities.stream()
                .filter(e -> {
                    // Универсальная проверка для всех типов Combatable
                    // Для CastleElement проверяем здоровье
                    if (e instanceof CastleElement) {
                        CastleElement castleElem = (CastleElement) e;
                        return !castleElem.isDestroyed() && castleElem.getHealth() < 100;
                    }
                    // Для других типов просто проверяем, что живы
                    return e.isAlive();
                })
                .collect(Collectors.toList());
    }

    public void sortByAttack() {
        entities.sort((e1, e2) -> Integer.compare(e2.getAttack(), e1.getAttack()));
    }

    public void sortByDefense() {
        entities.sort((e1, e2) -> Integer.compare(e2.getDefense(), e1.getDefense()));
    }

    public Map<String, Integer> getCombatSummary() {
        return entities.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getClass().getSimpleName(),
                        Collectors.summingInt(e -> e.isAlive() ? 1 : 0)
                ));
    }

    public int getTotalAttackPower() {
        return entities.stream()
                .filter(Combatable::isAlive)
                .mapToInt(Combatable::getAttack)
                .sum();
    }

    public int getTotalDefensePower() {
        return entities.stream()
                .filter(Combatable::isAlive)
                .mapToInt(Combatable::getDefense)
                .sum();
    }

    public int size() { return entities.size(); }
    public boolean isEmpty() { return entities.isEmpty(); }
    public void clear() {
        entities.clear();
        entityByName.clear();
    }

    @Override
    public String toString() {
        return String.format("GameCollection[%d entities, %d active, Attack: %d, Defense: %d]",
                entities.size(), getActiveEntities().size(),
                getTotalAttackPower(), getTotalDefensePower());
    }
}