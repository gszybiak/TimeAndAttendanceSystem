package szybiakg.loginPage.readout;

import lombok.Getter;

@Getter
public enum PresenceType {
    BREAK("Break"),
    PRESENCE("Presence"),
    PL("Paid leave"),
    SL("Sick leave"),
    UL("Unpaid leave"),
    HOLIDAY("Holiday");

    private final String description;

    PresenceType(String description) {
        this.description = description;
    }

}
