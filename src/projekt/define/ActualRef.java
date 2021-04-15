package projekt.define;

public class ActualRef {
    private int actualReference;
    private int position;
    private int timeRecentlyUsed;

    public ActualRef(int actualReference, int position, int timeRecentlyUsed) {
        this.actualReference = actualReference;
        this.position = position;
        this.timeRecentlyUsed = timeRecentlyUsed;
    }

    @Override
    public String toString() {
        return "Status aktualny: " +
                "\tOdolanie: " + actualReference +
                "\t\tCzas od ostatniego uzycia: " + timeRecentlyUsed;
    }

    public int getActualReference() {
        return actualReference;
    }

    public void setActualReference(int actualReference) {
        this.actualReference = actualReference;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTimeRecentlyUsed() {
        return timeRecentlyUsed;
    }

    public void setTimeRecentlyUsed(int timeRecentlyUsed) {
        this.timeRecentlyUsed = timeRecentlyUsed;
    }
}
