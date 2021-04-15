package projekt.define;

public class MinMaxFrame {
    private int max;
    private int min;
    private DefProcess proces;

    public MinMaxFrame(int min, int max, DefProcess proces) {
        this.min = min;
        this.max = max;
        this.proces = proces;
    }

    @Override
    public String toString() {
        return "min: " + min +
                "\tmax: " + max;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public DefProcess getProces() {
        return proces;
    }

    public void setProces(DefProcess proces) {
        this.proces = proces;
    }
}
