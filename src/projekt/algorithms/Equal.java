package projekt.algorithms;

import projekt.define.ActualRef;
import projekt.define.DefProcess;
import projekt.define.MinMaxFrame;

import java.util.ArrayList;

public class Equal { // Przydział równy
    DefProcess[] frames;
    ActualRef[] actualReference;
    ArrayList<DefProcess> defProcessList;
    int missingPage = 0;
    MinMaxFrame[] minMaxFrames;



    public Equal(DefProcess[] frames, ArrayList<DefProcess> defProcessList) {
        this.frames = frames;
        this.defProcessList = defProcessList;
        actualReference = new ActualRef[frames.length];
        minMaxFrames = new MinMaxFrame[defProcessList.size()]; // przechowuje informacje ile ramek jest dostepnych dla danego prcesu

        start();
    }


    private void start(){
        fullActualRef();
        int min = 0;

        // przydzielanie ilosci ramek dla kazdego procesu
        for (int i = 0; i < defProcessList.size(); i++) {

            int add = min + assingFrames(frames.length, defProcessList.size());

            minMaxFrames[i] = new MinMaxFrame(min, add, defProcessList.get(i));

            min =minMaxFrames[i].getMax();
        }

        startLRU();
    }



    // przydziela rowna ilosc ramek dla procesow
    private int assingFrames(int countFrames, int precesses){

        int resoult = countFrames/precesses;

        return resoult;
    }



    private void startLRU() {

        for (int i = 0; i < defProcessList.size(); i++) {

            for (int j = 0; j < defProcessList.get(i).getReferenceList().size(); j++) {


                doLRU(defProcessList.get(i) ,defProcessList.get(i).getReferenceList().get(j), minMaxFrames[i], j);
                showFrames();


                // kazdy dzialajacy proces z ramki dostaje +1 do jednoski czasu liczonej od ostaniego odwolania
                for (int k = minMaxFrames[i].getMin(); k < minMaxFrames[i].getMax(); k++) {

                    if (frames[k] !=null)
                        actualReference[k].setTimeRecentlyUsed( actualReference[k].getTimeRecentlyUsed()+1 );

                }
            }
        }
        System.out.println("\n---------------->\tBrakujacych stron: "+missingPage);
    }


    private void fullActualRef(){

        for (int i = 0; i < frames.length; i++)
            actualReference[i] = new ActualRef(0,0,0);

    }


    private void doLRU(DefProcess defProcess, int process, MinMaxFrame minMax, int indexReference){

        // sprawdzanie czy powtarza sie odwolanie
        for (int i = minMax.getMin(); i < minMax.getMax(); i++) {

            if (frames[i] != null && actualReference[i].getActualReference() == process){

                frames[i].setReferenceQuantity( frames[i].getReferenceQuantity() + 1 );

                actualReference[i].setTimeRecentlyUsed(0);
                return;
            }
        }


        // uzupelnianie pustych ramek jesli takie sa
        for (int i = minMax.getMin(); i < minMax.getMax(); i++) {

            if (frames[i] == null){
                frames[i] = defProcess;
                actualReference[i].setActualReference(process);
                actualReference[i].setPosition(indexReference);
                actualReference[i].setTimeRecentlyUsed(0);
                missingPage++;
                return;
            }
        }


        // zastepowanie ramki
        for (int i = minMax.getMin(); i < minMax.getMax(); i++) {

            int replaceNr = replecePage(minMax.getMin(), minMax.getMax(), indexReference);
            frames[replaceNr] = defProcess;
            actualReference[replaceNr].setActualReference(process);
            actualReference[replaceNr].setTimeRecentlyUsed(0);
            actualReference[replaceNr].setPosition(indexReference);
            missingPage++;
            return;
        }
    }


    // metoda ktora wybiera strone ktora zostanie zastapiona na podstawie tego kiedy ostatnio zostala uzyta
    private int replecePage(int min, int max, int indexReference) {
        int nr = min;
        int findMax = actualReference[min].getTimeRecentlyUsed();

        if (max - min == 1)
            return nr;


        for (int i = min; i < max; i++) {

            if (findMax < actualReference[i].getTimeRecentlyUsed()){
                findMax = actualReference[i].getTimeRecentlyUsed();
                nr = i;

            }
        }

        return nr;
    }


    private boolean checkFrames(int defProcess){

        for (int i = 0; i < frames.length; i++) {

            if (frames[i] == null)
                break;
            else if (actualReference[i].getActualReference() == defProcess)
                return true;
        }

        return false;
    }


    private void showFrames(){
        System.out.println("\nAktywne ramki:");

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != null)
                System.out.println(actualReference[i]+"\t\t"+frames[i]);
            else
                System.out.println("Ramka jest pusta");
        }
    }


    public String getMissingPage() {
        return "--> Brakujacych stron w przydziale rownym: "+missingPage;
    }
}
