package DataAccess;

import transport_module.Driver;

public class DriverDto {
    private String name;
    private String id;
    private String[]  licence;

    public String getName(){return name;}
    public String getId(){return id;}
    public String[] getLicence(){return licence;}

    public DriverDto(String name, String id, String[] licence){
        this.id = id;
        this.name = name;
        this.licence = licence;
    }


}
