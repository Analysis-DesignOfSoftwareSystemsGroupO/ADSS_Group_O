package Domain;

public class InformationContact {
    private String contactName;
    private String contactPhone;
    private String title;

    public InformationContact(String contactName, String contactPhone, String title) {
        if (contactName == null || contactPhone == null || title == null) {
            throw new NullPointerException("Contact Name and Contact Phone cannot be null");
        }
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.title = title;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        if (contactName == null || contactName.equals("")) {
            throw new NullPointerException("Contact Name cannot be null");
        }
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        if (contactPhone == null || contactPhone.equals("")) {
            throw new NullPointerException("Contact Phone cannot be null");
        }
        this.contactPhone = contactPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.equals("")) {
            throw new NullPointerException("Title cannot be null");
        }
        this.title = title;
    }

    @Override
    public String toString() {
        return "InformationContact [contactName=" + contactName + ", contactPhone=" + contactPhone + ", title=" + title + "]";
    }
}
