package DataAccess;

import transport_module.Driver;

public class DriverDto {
    private String name;
    private String id;
    private String[][2]  licence;

    public String getName(){return name;}
    public String getId(){return id;}
    public String[][2] getLicence(){return licence;}

    public DriverDto(String name, String id, String[][2] licence){
        this.id = id;
        this.name = name;
        this.licence = licence;
    }

}
