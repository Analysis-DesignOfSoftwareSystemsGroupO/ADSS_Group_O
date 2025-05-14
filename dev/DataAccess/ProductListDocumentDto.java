package DataAccess;

public class ProductListDocumentDto {

    //maybe put id
    //private id
    private String siteDes;
    private String[] products;
    private int[] quantity;
    private String date;
    public ProductListDocumentDto(String siteDes, String[] products, int[] quantity, String date) {
        this.siteDes = siteDes;
        this.products = products;
        this.quantity = quantity;
        this.date = date;
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

    public String getDate() {
        return date;
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

    public void setDate(String date) {
        this.date = date;
    }
}

