package transport_module;

/***
 * Product class representing an item that can be loaded onto a transport.
 * Each product has a unique code, a name, and a weight (in kilograms).
 */
public class Product {
    private int code;
    private String name;
    private int weight;


    /***
     * Default constructor - creates an empty Product.
     */
    public Product() {
        name = "";
        weight = 0;
        code = 0;
    }

    /***
     * Constructor - creates a Product with specified code, name, and weight.
     * @param c Code of the product
     * @param n Name of the product
     * @param w Weight of the product (in kilograms)
     */
    public Product(int c, String n, int w) {
        name = n;
        weight = w;
        code = c;

    }

    public Product(Product other) {
        if (other != null ) {
            weight = other.weight;
            name = other.name;
            code = other.code;
        }
    }
//********************************************************************************************************************** Get functions

    /***
     * @return Weight of the product
     */
    public int getWeight() {
        return weight;
    }

    /***
     * @return Name of the product
     */
    public String getName() {
        return name;
    }

    /***
     * @return Code of the product
     */
    public int getCode() {
        return code;
    }
//********************************************************************************************************************** print functions

    /***
     * @return A string representation of the product details
     */
    @Override
    public String toString() {
        return "Product: " + code + " " + name + " weights " + weight + " kg";

    }

    /***
     * Checks if two Product objects are equal based on their unique code.
     * @param obj Other object to compare
     * @return True if the codes match, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return this.code == other.code;
    }

    /***
     * @return Hash code based on the product's code
     */
    @Override
    public final int hashCode() {

        return this.getCode();
    }
}
