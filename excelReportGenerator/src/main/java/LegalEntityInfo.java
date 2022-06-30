public class LegalEntityInfo {
    private String name;

    public String getName() {
        return name;
    }

    private String separateEntityName;

    public String getSeparateEntityName() {
        return separateEntityName;
    }

    private String mailAddress;

    public String getMailAddress() {
        return mailAddress;
    }

    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    private String payerRegistrationNumber;

    public String getPayerRegistrationNumber() {
        return payerRegistrationNumber;
    }

    public LegalEntityInfo(String name, String separateEntityName, String mailAddress, String emailAddress, String payerRegistrationNumber) {
        this.name = name;
        this.separateEntityName = separateEntityName;
        this.mailAddress = mailAddress;
        this.emailAddress = emailAddress;
        this.payerRegistrationNumber = payerRegistrationNumber;
    }
}
