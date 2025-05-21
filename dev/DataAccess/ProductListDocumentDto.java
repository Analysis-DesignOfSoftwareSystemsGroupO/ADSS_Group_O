package DataAccess;

public class ProductListDocumentDto {

    //maybe put id
    //private id
    private int id;
    private String siteDes;
    private String[] products;
    private int[] quantity;

    public ProductListDocumentDto(int id, String siteDes, String[] products, int[] quantity) {
        this.siteDes = siteDes;
        this.products = products;
        this.quantity = quantity;
        this.id = id;

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSiteDes() {
        return siteDes;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getQuantity() {
        return quantity;
    }


    // Setters
    public void setSiteDes(String siteDes) {
        this.siteDes = siteDes;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }

}

