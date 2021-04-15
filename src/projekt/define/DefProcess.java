package projekt.define;

import java.util.ArrayList;

public class DefProcess {
    private String name;
    private ArrayList<Integer> referenceList = new ArrayList<>();
    private int arrivalTime;
    private int referenceQuantity;
    private ArrayList<Integer> timeRecentlyUsed;
    private int errors;
    private int sizeListElem;

    public DefProcess(String name, int arrivalTime, int reference, int listReference) {
        this.name = name;
        this.referenceList = generateReference(reference, listReference);
        this.arrivalTime = arrivalTime;
        this.referenceQuantity = 0;
        this.timeRecentlyUsed = new ArrayList<>();
        this.errors = 0;
        this.sizeListElem = referenceList.size();
    }

    @Override
    public String toString() {
        return  "Nazwa procesu: " + name +
                "\t\tCzas przyjscia do kolejki: " + arrivalTime +
                "\t\tilosc odwolan do strony: " + referenceQuantity +
                "\t\tOdwolania: " + referenceList;
    }

    private ArrayList<Integer> generateReference(int reference, int listReference){
        int sizeOfList = (int) (Math.random()*listReference);
        if (sizeOfList == 0)
            sizeOfList++;

        for (int i = 0; i < sizeOfList; i++) {

            int x = (int) (Math.random()*reference);
            if (x == reference)
                x = x-1;
            referenceList.add(x);

        }
        return referenceList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getReferenceQuantity() {
        return referenceQuantity;
    }

    public void setReferenceQuantity(int referenceQuantity) {
        this.referenceQuantity = referenceQuantity;
    }

    public void setReferenceList(ArrayList<Integer> referenceList) {
        this.referenceList = referenceList;
    }

    public ArrayList<Integer> getTimeRecentlyUsed() {
        return timeRecentlyUsed;
    }

    public void setTimeRecentlyUsed(ArrayList<Integer> timeRecentlyUsed) {
        this.timeRecentlyUsed = timeRecentlyUsed;
    }

    public ArrayList<Integer> getReferenceList() {
        return referenceList;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getSizeListElem() {
        return sizeListElem;
    }

    public void setSizeListElem(int sizeListElem) {
        this.sizeListElem = sizeListElem;
    }

    public void decreseSizeList(){
        setSizeListElem( getSizeListElem()-1 );
    }
}
