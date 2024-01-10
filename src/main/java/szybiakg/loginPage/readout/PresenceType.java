package szybiakg.loginPage.readout;

public enum PresenceType {
    B("Break"),
    P("Presence"),
    PL("Paid leave"),
    SL("Sick leave"),
    UL("Unpaid leave");

    private final String description;

    PresenceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
