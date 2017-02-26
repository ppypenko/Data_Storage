package team.afalse.data_storage;

/**
 * Created by Kyle on 2/23/2017.
 */

public enum Month {
    JANUARY("January", 31),
    FEBRUARY("February", 28),
    MARCH("March", 31),
    APRIL("April", 30),
    MAY("May", 31),
    JUNE("June", 30),
    JULY("July", 31),
    AUGUST("August", 30),
    SEPTEMBER("September", 31),
    OCTOBER("October", 30),
    NOVEMBER("November", 31),
    DECEMBER("December", 30);

    private String displayText;
    private int days;

    private Month(String displayText, int days) {
        this.displayText = displayText;
        this.days = days;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getDays() {
        return days;
    }

    @Override
    public String toString() {
        return displayText;
    }

}
