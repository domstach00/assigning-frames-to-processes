package projekt;


import projekt.algorithms.ControlMissing;
import projekt.algorithms.Equal;
import projekt.algorithms.Proportional;
import projekt.define.DefProcess;

import java.util.ArrayList;

public class AssigningFrames {
    DefProcess[] frames;
    int numberOfProcesses;
    ArrayList<DefProcess> defProcessList = new ArrayList<>();
    Proportional proportional;
    Equal equal;
    ControlMissing controlMissing;


    public AssigningFrames(int pageFrames, int numberOfProcesses, int reference, int listReference) {
        this.numberOfProcesses = numberOfProcesses;
        this.frames = new DefProcess[pageFrames];

        System.out.println("Liczba ramek: "+ pageFrames);
        generateProcesses(reference, listReference);

        showList();

        propAlg();      // Przydział proporcjonalny
        equalAlg();     // Przydział równy
        controllAlg();  // Sterowanie częstością błędów strony

        compareAlgs();  // porownanie wynikow
    }
    //1. Przydział proporcjonalny
    //2. Przydział równy
    //3. Sterowanie częstością błędów strony


    private void propAlg(){
        proportional = new Proportional(frames, defProcessList);
    }

    private void equalAlg(){
        equal = new Equal(frames, defProcessList);
    }

    private void controllAlg(){
        controlMissing = new ControlMissing(frames, defProcessList);
    }



    private void compareAlgs() {
        System.out.println("\n");
        System.out.println("Porownanie algorytmow wzgledem ilosci stron ktore trzeba bylo zaladowac do ramki");
        System.out.println(proportional.getMissingPage());
        System.out.println(equal.getMissingPage());
        System.out.println(controlMissing.getMissingPage());


    }

    private void generateProcesses(int reference, int listReference) {

        for (int i = 0; i < numberOfProcesses; i++)
            defProcessList.add(new DefProcess("Proces"+(1+i), i, reference, listReference));
    }


    private void showList(){

        for (int i = 0; i < defProcessList.size(); i++)
            System.out.println(defProcessList.get(i));
    }

}
