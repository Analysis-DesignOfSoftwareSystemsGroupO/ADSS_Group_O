package transport_module;

public class DrivingLicence {
    private String description;
    private String code;

    public DrivingLicence(String description , String code){
        this.description = description;
        this.code = code;
    }

    /**
     * Copy constructor
     * @param other DrivingLicence instance
     */
    public DrivingLicence(DrivingLicence other){
        this.code = other.code;
        this.description = other.description;
    }

    /**
     * @param o some object, if it is a driving licence reference, than compare the two code licences.
     * @return true or false by the compersion
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrivingLicence d = (DrivingLicence) o;
        if(code == d.code){return true;}
        return false;
    }
}
