package szybiakg.loginPage.readout;

import lombok.Getter;

@Getter
public enum PresenceType {
    B("Break"),
    P("Presence"),
    PL("Paid leave"),
    SL("Sick leave"),
    UL("Unpaid leave"),
    H("Holiday");

    private final String description;

    PresenceType(String description) {
        this.description = description;
    }

}
