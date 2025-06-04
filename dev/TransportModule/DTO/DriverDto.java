package TransportModule.DTO;

import java.util.List;

public class DriverDto {
    private String id;
    private List<String>  licence;

    public String getId(){return id;}
    public List<String> getLicence(){return licence;}

    public DriverDto( String id, List<String> licence){
        this.id = id;
        this.licence = licence;
    }


}
