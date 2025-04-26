package transport_module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private ArrayList<DrivingLicence> licencs;
    private final String name;
    private final String id;
    private Map<LocalDate, Boolean> availablityCalander;

    public Driver(String name, String id, ArrayList<DrivingLicence> licencs) {
        this.name = name;
        this.id = id;
        this.licencs = new ArrayList<>();
        for (DrivingLicence licence : licencs) {
            this.licencs.add(new DrivingLicence(licence));
        }
        availablityCalander = new HashMap<>();
    }

    /**
     * @return Copy of the Driving licence list of the driver
     */
    public ArrayList<DrivingLicence> getLicencs() { //return copy of the list
        ArrayList<DrivingLicence> cpy = new ArrayList<DrivingLicence>();
        for (DrivingLicence dl : licencs) {
            cpy.add(new DrivingLicence(dl));
        }
        return cpy;
    }

    public void assignToMission(LocalDate date) {
        availablityCalander.put(date, true);

    }

    public void release(LocalDate date) {
        availablityCalander.remove(date);

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isavailable(LocalDate date) {
        return availablityCalander.get(date) == null;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", licenses=" + licencs +
                '}';
    }

}
