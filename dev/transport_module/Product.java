package transport_module;

public class Product {
    private String name;
    private int weight;


    public Product(){
        name = "";
        weight = 0;
    }
    public Product(String n, int w){
        name = n;
        weight = w;

    }
    public Product(Product other){
        if(other!=null){
            weight = other.weight;
            name = other.name;
        }
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }
    public void addWeight(int w){
        if(w>0)
            weight+=w;
    }
    @Override
    public String toString(){
        return name + " " + weight ;

    }
    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return this.name.equals(other.name);
    }
}
