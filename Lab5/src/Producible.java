public interface Producible {
    int produce();
    int getProductionRate();
    boolean canProduce();
    int getProductionCost();
    String getProductType();
}