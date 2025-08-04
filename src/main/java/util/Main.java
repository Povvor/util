package util;
public class Main {

    public static void main(String[] args) {
        CoreLogic coreLogic = new CoreLogic();
        coreLogic.parseArgs(args);
        coreLogic.processFiles();
        coreLogic.writeAllAndShowStatistics();
    }
}