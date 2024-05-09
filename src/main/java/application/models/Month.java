package application.models;

import java.util.ArrayList;
import java.util.List;

public enum Month {
    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    private final String displayName;

    Month(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<String> getAllDisplayNames() {
        List<String> displayNames = new ArrayList<>();
        for (Month month : Month.values()) {
            displayNames.add(month.getDisplayName());
        }
        return displayNames;
    }

    public static Month fromString(String text) {
        StringBuilder validNames = new StringBuilder();
        for (Month month : Month.values()) {
            if (month.displayName.equalsIgnoreCase(text)) {
                return month;
            }
            validNames.append(month.displayName).append(", ");
        }
        String errorMessage = "No constant with display name " + text + " found. It should be one of these: " +
                validNames.substring(0, validNames.length() - 2);
        throw new IllegalArgumentException(errorMessage);
    }


}
