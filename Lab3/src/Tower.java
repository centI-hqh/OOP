public class Tower extends CastleElement {
    private int arrowCapacity;
    private int currentArrows;

    public Tower(String name, int health, int attack, int defense, int arrowCapacity) {
        super(name, health, attack, defense);
        this.arrowCapacity = arrowCapacity;
        this.currentArrows = arrowCapacity;
    }

    @Override
    public boolean canAttack(Combatable target) {
        return super.canAttack(target) && currentArrows > 0;
    }

    @Override
    public int attack(Combatable target) {
        if (canAttack(target)) {
            currentArrows--;
            int damage = calculateDamage();
            target.takeDamage(damage);
            System.out.println(name + " выпускает стрелу! Урон: " + damage);
            return damage;
        }
        return 0;
    }

    public void resupplyArrows(int amount) {
        currentArrows = Math.min(arrowCapacity, currentArrows + amount);
        System.out.println(name + " пополнена стрелами: " + currentArrows + "/" + arrowCapacity);
    }

    public int getCurrentArrows() {
        return currentArrows;
    }
}