public class Tower extends CastleElement {
    private int arrowCapacity;
    private int currentArrows;
    private boolean canShoot;

    public Tower(String name, int health, int defense, int arrowCapacity) {
        super(name, health, defense);
        this.arrowCapacity = arrowCapacity;
        this.currentArrows = arrowCapacity;
        this.canShoot = true;
    }

    @Override
    public void defend() {
        if (!isDestroyed && canShoot && currentArrows > 0) {
            currentArrows--;
            System.out.println(name + " выпускает стрелу! Осталось стрел: " + currentArrows);
        } else if (currentArrows <= 0) {
            System.out.println(name + ": нет стрел для обороны!");
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (!isDestroyed) {
            int actualDamage = Math.max(1, damage - defense);
            health -= actualDamage;
            System.out.println(name + " получает урон " + actualDamage);

            if (health <= 0) {
                isDestroyed = true;
                canShoot = false;
                System.out.println(name + " разрушена!");
            }

            // Башня не может стрелять при серьезных повреждениях
            if (getHealthPercentage() < 40 && canShoot) {
                canShoot = false;
                System.out.println(name + " не может стрелять из-за повреждений!");
            }
        }
    }

    @Override
    public String getStatus() {
        if (isDestroyed) return "[РАЗРУШЕНА]";
        if (!canShoot) return "[НЕ СТРЕЛЯЕТ]";
        if (currentArrows == 0) return "[НЕТ СТРЕЛ]";
        return "[ГОТОВА К ОБОРОНЕ]";
    }

    public void resupplyArrows(int amount) {
        currentArrows = Math.min(arrowCapacity, currentArrows + amount);
        System.out.println(name + " пополнена стрелами: " + currentArrows + "/" + arrowCapacity);
    }

}