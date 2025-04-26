package transport_module;

public class DrivingLicence {
    private final String description;
    private final String code;

    public DrivingLicence(String description, String code) {
        this.description = description;
        this.code = code;

    }

    /**
     * Copy constructor
     *
     * @param other DrivingLicence instance
     */
    public DrivingLicence(DrivingLicence other) {
        this.code = other.code;
        this.description = other.description;
    }

    public String getCode() {
        return code;
    }


    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + " : " + description;
    }

    /**
     * @param o some object, if it is a driving licence reference, than compare the two code licences.
     * @return true or false by the compersion
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrivingLicence d = (DrivingLicence) o;
        return code.equals(d.code);
    }

    @Override
    public final int hashCode() {
        return this.code != null ? this.code.hashCode() : 0;
    }
}
