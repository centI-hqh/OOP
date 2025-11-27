public interface Combatable {
    int attack(Combatable target);
    void takeDamage(int damage);
    boolean canAttack(Combatable target);
    int getAttack();
    int getDefense();
    boolean isAlive();
}