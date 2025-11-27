import java.util.*;
import java.util.stream.Collectors;

public class CastleDefenseManager {
    private TreeMap<String, CastleElement> defenses;
    private TreeMap<Integer, List<CastleElement>> defensesByLevel;

    public CastleDefenseManager() {
        this.defenses = new TreeMap<>();
        this.defensesByLevel = new TreeMap<>();
    }

    public void addDefense(CastleElement defense) {
        defenses.put(defense.getName(), defense);
        defensesByLevel.computeIfAbsent(defense.getLevel(), k -> new ArrayList<>()).add(defense);
    }

    public CastleElement getDefense(String name) {
        return defenses.get(name);
    }

    public List<CastleElement> getDefensesByLevel(int level) {
        return defensesByLevel.getOrDefault(level, new ArrayList<>());
    }

    public SortedMap<String, CastleElement> getDefensesInRange(String from, String to) {
        return defenses.subMap(from, to);
    }

    public Map<Integer, Integer> getLevelDistribution() {
        Map<Integer, Integer> distribution = new TreeMap<>();
        defensesByLevel.forEach((level, list) -> distribution.put(level, list.size()));
        return distribution;
    }

    public CastleElement getStrongestDefense() {
        return defenses.values().stream()
                .filter(d -> !d.isDestroyed())
                .max(Comparator.comparingInt(CastleElement::getDefense))
                .orElse(null);
    }

    public CastleElement getWeakestDefense() {
        return defenses.values().stream()
                .filter(d -> !d.isDestroyed())
                .min(Comparator.comparingInt(CastleElement::getDefense))
                .orElse(null);
    }

    public List<CastleElement> getDamagedDefenses() {
        return defenses.values().stream()
                .filter(d -> !d.isDestroyed() && d.getHealth() < d.getHealth() * 0.5)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("CastleDefenseManager[%d defenses, levels: %s]",
                defenses.size(), getLevelDistribution());
    }
}