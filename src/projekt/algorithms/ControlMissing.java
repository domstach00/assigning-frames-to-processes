package projekt.algorithms;

import projekt.define.ActualRef;
import projekt.define.DefProcess;
import projekt.define.MinMaxFrame;

import java.util.ArrayList;

public class ControlMissing { // Sterowanie częstością błędów strony
    DefProcess[] frames;
    ActualRef[] actualReference;
    ArrayList<DefProcess> defProcessList;
    int missingPage = 0;
    MinMaxFrame[] minMaxFrames;


    public ControlMissing(DefProcess[] frames, ArrayList<DefProcess> defProcessList) {
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

            int add = min + 1;

            minMaxFrames[i] = new MinMaxFrame(min, add, defProcessList.get(i));

            min = minMaxFrames[i].getMax();
        }

        startLRU();
    }



    private void showMinMax(){

        for (int i = 0; i < minMaxFrames.length; i++) {
            System.out.println(minMaxFrames[i].getMin()+"\t"+minMaxFrames[i].getMax());
        }
    }


    private void startLRU() {

        int[] sizeList = new int[defProcessList.size()];

        for (int i = 0; i < defProcessList.size(); i++)
            sizeList[i] = defProcessList.get(i).getReferenceList().size();

        int max = findMax(sizeList);
        int t = 0;

        for (int i = 0; i < defProcessList.size(); i++) {

            while (t < max){

                for (int j = 0; j < defProcessList.size(); j++) {

                    if (sizeList[j] > t){

                        doLRU(defProcessList.get(j), defProcessList.get(j).getReferenceList().get(t), minMaxFrames[j], t);
                        showFrames();

                        //kazdy dzialajacy proces z ramki dostaje +1 do jednoski czasu liczonej od ostaniego odwolania
                        for (int k = 0; k < frames.length; k++) {

                            if (frames[k] != null)
                                actualReference[k].setTimeRecentlyUsed( actualReference[k].getTimeRecentlyUsed()+1 );

                        }

                        controlLimits();
                    }
                }
                t++;
            }
        }
        System.out.println("\n---------------->\tBrakujacych stron: "+missingPage);

    }


    // metoda kontroluje ilosc przydzielonych ramek dla danego procesu
    private void controlLimits(){


        for (int i = 0; i < minMaxFrames.length; i++) {

            // sprawdzenie czy jakis z procesow potrzebuje wiecej ramek
            if (isInDomainUp() && isUpper(minMaxFrames[i].getProces())){
                frames = riseRange(frames, minMaxFrames[i].getMax()); // przesuniecie elementow w liscie frames o 1 do przodu od miejsca minMaxFrames[i].getMax()
                actualReference = riseRange(actualReference, minMaxFrames[i].getMax()); // przesuniecie elementow w liscie actualReference o 1 do przodu od miejsca minMaxFrames[i].getMax()
                frames[minMaxFrames[i].getMax()] = null;

                minMaxFrames[i].setMax( minMaxFrames[i].getMax()+1 ); // przesuniecie zakresu maksymalnego o +1 dla i-tego procesu
                minMaxFrames[i].getProces().setErrors(0);

                // przesuniecie zakresu ramek dla procesow ktore znajduja sie wyzej niz ten ktoremu przyznajemy miejsce
                for (int j = i+1; j < minMaxFrames.length; j++) {
                    minMaxFrames[j].setMin( minMaxFrames[j].getMin()+1 );
                    minMaxFrames[j].setMax( minMaxFrames[j].getMax()+1 );
                }
            }
            // sprawdzenie czy jakis z procesow potrzebuje mniej ramek
            else if (isInDomainDown() && isLower(minMaxFrames[i].getProces())){
                int clearFrame = replecePage(minMaxFrames[i].getMin(),minMaxFrames[i].getMax()); // ramka do odebrania zostaje wyznaczona na podstawie LRU
                frames = fallRange(frames, clearFrame); // przesuniecie elementow w liscie frames o 1 do tylu od miejsca clearFrame
                actualReference = fallRange(actualReference, clearFrame); // przesuniecie elementow w liscie actualReference o 1 do tylu od miejsca clearFrame
                frames[clearFrame] = null;

                minMaxFrames[i].setMax( minMaxFrames[i].getMax()-1 ); // przesuniecie zakresu maksymalnego o -1 dla i-tego procesu
                minMaxFrames[i].getProces().setErrors(0);

                // przesuniecie zakresu ramek dla procesow ktore znajduja sie wyzej niz ten ktoremu odebralismy miejsce
                for (int j = 0; j < minMaxFrames.length; j++) {
                    minMaxFrames[j].setMin( minMaxFrames[j].getMin()-1 );
                    minMaxFrames[j].setMax( minMaxFrames[j].getMax()-1 );
                }
            }
        }
    }


    // sprawdzamy czy mozna przesunac tablice o jeden w gore
    private boolean isInDomainUp(){

        for (int i = 0; i < minMaxFrames.length; i++) {

            if (minMaxFrames[i].getMax()+1 >= frames.length)
                return false;
        }
        return true;
    }

    // sprawdzamy czy mozna przesunac tablice o jeden w dol
    private boolean isInDomainDown(){

        for (int i = 0; i < minMaxFrames.length; i++) {

            if ( (minMaxFrames[i].getMax()-minMaxFrames[i].getMin() <= 1 && minMaxFrames[i].getMin()-1 <= 0))
                return false;
            else if (minMaxFrames[i].getMin()-1 <= 0)
                return false;
        }

        return true;
    }


    // metoda ktora sprawdza czy dany proces potrzebuje wiecej ramek
    private boolean isUpper(DefProcess defProcess){
        if (defProcess==null)
            return false;

        double errors = (double) defProcess.getErrors();
        double sizeRefer = (double) defProcess.getSizeListElem();

        if ( (errors/sizeRefer) >= 0.5)
            return true;
        return false;
    }

    // metoda ktora sprawdza czy dany proces potrzebuje mniej ramek
    private boolean isLower(DefProcess defProcess){
        if (defProcess==null)
            return false;

        double errors = (double) defProcess.getErrors();
        double sizeRefer = (double) defProcess.getSizeListElem();

        if ( (errors / sizeRefer) <= 0.25)
            return true;
        return false;
    }


    // metoda przesowajaca elementy w liscie o jeden do przodu od miejsca 'start'
    private DefProcess[] riseRange(DefProcess[] arr, int start){
        DefProcess[] newArr = new DefProcess[arr.length];

        if (arr[arr.length - 1] != null)
            return arr;

        for (int i = 0; i < start; i++)
            newArr[i] = arr[i];

        for (int i = start; i < arr.length - 1; i++)
            newArr[i + 1] = arr[i];

        return newArr;

    }
    // metoda przesowajaca elementy w liscie o jeden do przodu od miejsca 'start'
    private ActualRef[] riseRange(ActualRef[] arr, int start){
        ActualRef[] newArr = new ActualRef[arr.length];

        if (arr[arr.length - 1] != null)
            return arr;

        for (int i = 0; i < start; i++)
            newArr[i] = arr[i];

        for (int i = start; i < arr.length - 1; i++)
            newArr[i + 1] = arr[i];

        return newArr;
    }

    // metoda przesowajaca elementy w liscie o jeden do tylu od miejsca 'start'
    private DefProcess[] fallRange(DefProcess[] arr, int start){
        DefProcess[] newArr = new DefProcess[arr.length];

        if (arr[arr.length-1] != null)
            return arr;

        for (int i = 0; i < start; i++)
            newArr[i] = arr[i];

        for (int i = arr.length-1; i > start; i--)
            newArr[i-1] = arr[i];

        return newArr;
    }
    // metoda przesowajaca elementy w liscie o jeden do tylu od miejsca 'start'
    private ActualRef[] fallRange(ActualRef[] arr, int start){
        ActualRef[] newArr = new ActualRef[arr.length];

        if (arr[arr.length-1] != null)
            return arr;

        for (int i = 0; i < start; i++)
            newArr[i] = arr[i];

        for (int i = arr.length-1; i > start; i--)
            newArr[i-1] = arr[i];

        return newArr;
    }

    // uzupelnienie listy actualReference pustymi miejscami
    private void fullActualRef(){

        for (int i = 0; i < frames.length; i++)
            actualReference[i] = new ActualRef(0,0,0);
    }


    // wykonywanie LRU
    private void doLRU(DefProcess defProcess, int process, MinMaxFrame minMax, int indexReference){

        // sprawdzanie czy powtarza sie odwolanie
        for (int i = minMax.getMin(); i < minMax.getMax(); i++) {

            if (frames[i] != null && actualReference[i].getActualReference() == process){

                frames[i].setReferenceQuantity( frames[i].getReferenceQuantity() + 1 ); // +1 do czestosci odwolac dla tej strony

                actualReference[i].setTimeRecentlyUsed(0); // zerowanie czasu pokazujacego ile minelo od ostatniego odwolania

                frames[i].decreseSizeList();
                return;
            }
        }


        // uzupelnianie pustych ramek jesli takie sa
        for (int i = minMax.getMin(); i < minMax.getMax(); i++) {

            if (frames[i] == null && i < 60){
                frames[i] = defProcess;
                actualReference[i].setActualReference(process); // ustawienie informacji o procesie i odwolaniu ktore jest w ramce
                actualReference[i].setPosition(indexReference);
                actualReference[i].setTimeRecentlyUsed(0); // zerowanie czasu pokazujacego ile minelo od ostatniego odwolania

                frames[i].setErrors( frames[i].getErrors()+1 ); // zwiekszenie ilosci bledow dla danego procesu
                frames[i].decreseSizeList();

                missingPage++;
                return;
            }
        }


        // zastepowanie ramki
        for (int i = minMax.getMin(); i < minMax.getMax(); i++) {

            int replaceNr = replecePage(minMax.getMin(), minMax.getMax()); // metoda zwraca index ramki z ktorej nalezy usunac strone na podstawie LRU
            frames[replaceNr] = defProcess; // przypisanie nowego procesu do ramki

            actualReference[replaceNr].setActualReference(process); // ustawienie informacji o procesie i odwolaniu ktore jest w ramce
            actualReference[replaceNr].setTimeRecentlyUsed(0); // zerowanie czasu pokazujacego ile minelo od ostatniego odwolania
            actualReference[replaceNr].setPosition(indexReference);

            frames[i].setErrors( frames[i].getErrors()+1 ); // zwiekszenie ilosci bledow dla danego procesu
            frames[i].decreseSizeList();

            missingPage++;
            return;
        }
    }


    // metoda ktora wybiera strone ktora zostanie zastapiona na podstawie tego kiedy ostatnio zostala uzyta
    private int replecePage(int min, int max) {
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

    // metoda szuka wartosci najwiekszej z listy
    private int findMax(int[] list){
        int temp = 0;

        for (int i = 0; i < list.length; i++) {

            if (temp < list[i])
                temp = list[i];
        }
        return temp;
    }


    // pokazywanie stanu ramek
    private void showFrames(){
        System.out.println("\nAktywne ramki:");

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != null)
                System.out.println(actualReference[i]+"\t\t"+frames[i]);
            else
                System.out.println("Ramka jest pusta");
        }
    }


    // wiadomosc koncowa
    public String getMissingPage() {
        return "--> Brakujacych stron w przydziale ze sterowana częstoscia bledow strony "+missingPage;
    }


}
