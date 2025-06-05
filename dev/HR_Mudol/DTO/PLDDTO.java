package HR_Mudol.DTO;

import java.util.Map;

public class PLDDTO {
    private final int id;
    private final String destination;
    private final Map<String, Integer> products;
    private final int totalWeight;

    public PLDDTO(int id, String destination, Map<String, Integer> products, int totalWeight) {
        this.id = id;
        this.destination = destination;
        this.products = products;
        this.totalWeight = totalWeight;
    }

    public int getId() { return id; }
    public String getDestination() { return destination; }
    public Map<String, Integer> getProducts() { return products; }
    public int getTotalWeight() { return totalWeight; }
}
