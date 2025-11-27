public interface Movable {
    boolean moveTo(int x, int y);
    boolean canMoveTo(int x, int y);
    int getMovementRange();
    int getX();
    int getY();
}