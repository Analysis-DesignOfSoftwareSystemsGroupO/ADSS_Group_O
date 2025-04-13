package transport_module;

public class Site {
    private String name;
    private String area;

    public Site(){
        name = "";
        area = "";
    }
    public Site(String n, String a){
        name = n;
        area = a;
    }
    public Site(Site other){
        if(other!= null){
            name = other.name;
            area = other.area;
        }
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }
}
