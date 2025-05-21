package DataAccess;

public class ProductListDocumentDto {

    //maybe put id
    //private id
    private String siteDes;
    private String[] products;
    private int[] quantity;

    public ProductListDocumentDto(String siteDes, String[] products, int[] quantity) {
        this.siteDes = siteDes;
        this.products = products;
        this.quantity = quantity;

    }

    // Getters
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

