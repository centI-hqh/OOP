public class Wall extends CastleElement {
    private int height;

    public Wall(String name, int health, int attack, int defense, int height) {
        super(name, health, attack, defense);
        this.height = height;
    }

    @Override
    public boolean canAttack(Combatable target) {
        return false;
    }

    @Override
    public int attack(Combatable target) {
        System.out.println(name + " не может атаковать!");
        return 0;
    }

    public int getHeight() {
        return height;
    }
}