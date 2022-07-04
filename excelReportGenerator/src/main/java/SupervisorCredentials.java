public class SupervisorCredentials {
    private final String title;
    private final String initials;
    private final String contactInfo;
    private final String date;

    public String getTitle() {
        return title;
    }

    public String getInitials() {
        return initials;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDate() {
        return date;
    }

    public SupervisorCredentials(String title, String initials, String contactInfo, String date) {
        this.title = title;
        this.initials = initials;
        this.contactInfo = contactInfo;
        this.date = date;
    }

    public SupervisorCredentials(String title, String initials, String date) {
        this.title = title;
        this.initials = initials;
        this.contactInfo = null;
        this.date = date;
    }
}

