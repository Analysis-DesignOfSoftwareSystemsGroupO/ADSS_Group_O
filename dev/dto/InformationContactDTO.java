package dto;

public class InformationContactDTO {
    private String contactName;
    private String contactPhone;
    private String title;
    public InformationContactDTO(String contactName, String contactPhone, String title) {
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.title = title;
    }
    public String getContactName() {
        return contactName;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public String getTitle() {
        return title;
    }
}
