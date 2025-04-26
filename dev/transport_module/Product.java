package transport_module;

public class Product {
    private int code;
    private String name;
    private int weight;


    public Product() {
        name = "";
        weight = 0;
        code = 0;
    }

    public Product(int c, String n, int w) {
        name = n;
        weight = w;
        code = c;

    }

    public Product(Product other) {
        if (other != null) {
            weight = other.weight;
            name = other.name;
            code = other.code;
        }
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Product: " + code + " " + name + " weights " + weight + " kg";

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return this.code == other.code;
    }
}
