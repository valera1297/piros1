import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class AbstractRunProcess {
    static int volumeData;
    static int timeDefrag;
    static Process massProcess[];
    static int countProcess;
    static Process massActivProcess[];

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.print("введите обьем памяти: ");
        volumeData = in.nextInt();
        System.out.print("введите время дефрагментации: ");
        timeDefrag = in.nextInt();
        File listProcess = new File("listProcess.txt");
        Scanner inFile = new Scanner(listProcess);
        countProcess = inFile.nextInt();
        massProcess = new Process[countProcess];
        for (int i = 0; i < countProcess; i++) {
            Process tempProcess = new Process();
            tempProcess.number = i;
            tempProcess.timeStart = inFile.nextInt();
            tempProcess.timeRun = inFile.nextInt();
            tempProcess.valueData = inFile.nextInt();
            massProcess[i] = tempProcess;
        }
        Arrays.sort(massProcess);
        firstFit();
        for (int i = 0; i < countProcess; i++) {
            System.out.println("time start run = " + (massProcess[i].timeEndRun - massProcess[i].timeStartRun)
                    + "defrag=" + massProcess[i].defrag);
        }
    }

    static public void defrag() {
        int tempIndex = 0;
        Process tempProcess[] = new Process[volumeData];
        for (int i = 0; i < volumeData; i++) {
            if (massActivProcess[i] != null) {
                tempProcess[tempIndex] = massActivProcess[i];
                tempIndex++;
            }
        }
        massActivProcess = tempProcess;
    }

    static public void destroyProcess(Process process) {
        for (int i = 0; i < volumeData; i++) {
            if (massActivProcess[i] == process) {
                massActivProcess[i] = null;
            }
        }
    }

    static public void firstFit() {
        massActivProcess = new Process[volumeData];
        int nullData = volumeData;
        int successProses = 0;
        int time = 0;
        int headProcess = 0;
        while (successProses != countProcess) {
            for (int i = 0; i < countProcess; i++) {
                if (massProcess[i].timeStartRun != -1 && massProcess[i].timeEndRun == -1) {
                    if (massProcess[i].timeCountRun == massProcess[i].timeRun) {
                        destroyProcess(massProcess[i]);
                        massProcess[i].timeEndRun = time;
                        nullData += massProcess[i].valueData;
                        successProses++;
                    } else {
                        massProcess[i].timeCountRun++;
                    }
                }
            }
            while (massProcess.length > headProcess
                    && massProcess[headProcess].timeStart <= time
                    && nullData >= massProcess[headProcess].valueData) {

                w:
                while (true) {
                    int tempStart = 0;
                    int tempCountNull = 0;
                    for (int i = 0; i < volumeData; i++) {
                        if (massActivProcess[i] == null) {
                            tempCountNull++;
                            if (tempCountNull >= massProcess[headProcess].valueData) {
                                tempStart = i - tempCountNull + 1;
                                break;
                            }
                        } else {
                            tempCountNull = 0;
                        }

                    }
                    if (tempCountNull >= massProcess[headProcess].valueData) {
                        massProcess[headProcess].timeStartRun = time;
                        massProcess[headProcess].timeCountRun = 0;
                        for (int i = tempStart; i < tempStart + massProcess[headProcess].valueData; i++) {
                            massActivProcess[i] = massProcess[headProcess];
                        }
                        nullData -= massProcess[headProcess].valueData;
                        headProcess++;
                        break w;
                    } else {
                        time += timeDefrag;
                        massProcess[headProcess].defrag = true;
                        defrag();
                    }
                }
            }
            time++;
        }
    }
}
