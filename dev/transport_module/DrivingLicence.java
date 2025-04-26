package transport_module;

/***
 * DrivingLicence class represents a type of driving licence.
 * Each licence has a description (like "Medium truck") and a code (like "C1").
 */
public class DrivingLicence {
    private  String description;
    private  String code;

    /***
     * Constructor - creates a DrivingLicence with given description and code.
     * @param description Description of the licence
     * @param code Licence code
     */
    public DrivingLicence(String description, String code) {
        this.description = description;
        this.code = code;

    }

    /***
     * Copy constructor - creates a new DrivingLicence from another instance.
     * @param other DrivingLicence instance to copy
     */
    public DrivingLicence(DrivingLicence other)
    {
        if(other!= null) {
            this.code = other.code;
            this.description = other.description;
        }
    }
//********************************************************************************************************************** Get functions

    /***
     * @return Licence code
     */
    public String getCode() {
        return code;
    }

    /***
     * @return Licence description
     */
    public String getDescription() {
        return description;
    }
//********************************************************************************************************************** print functions

    /***
     * @return String representation of the DrivingLicence (code and description)
     */
    @Override
    public String toString() {
        return code + " : " + description;
    }

    /***
     * Compares this DrivingLicence to another object based on the licence code.
     * @param o Object to compare
     * @return true if codes match, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrivingLicence d = (DrivingLicence) o;
        return code.equals(d.code);
    }

    /***
     * @return Hash code based on the licence code
     */
    @Override
    public final int hashCode() {
        return this.code != null ? this.code.hashCode() : 0;
    }
}
