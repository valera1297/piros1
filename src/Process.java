public class Process implements Comparable<Process>{
    int number;
    int timeStart;
    int timeRun;
    int valueData;
    int timeStartRun = -1;
    int timeCountRun = 0;
    int timeEndRun = -1;
    boolean defrag = false;
    public int compareTo(Process process) {
        return this.timeStart - process.timeStart;
    }
}
