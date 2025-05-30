package transport_module;

import java.util.Objects;

/***
 * Site class representing a physical location in the system.
 * Each Site has a name and an area (North, Center, South).
 */
public class Site {
    private String name;
    private String area;

    /***
     * Default constructor - creates an empty Site instance.
     */
    public Site(){
        name = "";
        area = "";
    }
    /***
     * Constructor - creates a Site instance with specified name and area.
     * @param n Name of the site
     * @param a Area of the site
     */
    public Site(String n, String a){
        name = n;
        area = a;
    }
    /***
     * Copy constructor - creates a new Site as a copy of another Site.
     * @param other Site instance to copy from
     */
    public Site(Site other){
        if(other!= null){
            name = other.name;
            area = other.area;
        }
    }
//********************************************************************************************************************** Get functions

    /***
     * @return The name of the site
     */
    public String getName() {
        return name;
    }
    /***
     * @return The area where the site is located
     */
    public String getArea() {
        return area;
    }

//********************************************************************************************************************** Print functions

    /***
     * Prints a simple description of the Site (area and name).
     * @return Formatted string of the Site
     */
    @Override
    public String toString(){
        return area+" : "+name;
    }

    /***
     * Compares two Site objects based on area and name.
     * @param other Another object to compare
     * @return True if equal, otherwise false
     */
    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Site that = (Site) other;
        return that.area.equals(area) && that.name.equals(name);

    }

    /***
     * @return Hash code based on name and area
     */
    @Override
    public final int hashCode() {
        return Objects.hash(name, area);

    }
}
