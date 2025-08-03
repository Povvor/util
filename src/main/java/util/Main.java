package util;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CoreLogic coreLogic = new CoreLogic();
        coreLogic.parseArgs(args);
        List<String> inputStrings = coreLogic.initInputStrings();
        if (inputStrings.isEmpty()) {
            return;
        }
        for(String string : inputStrings) {
            coreLogic.processString(string);
        }
        coreLogic.writeAllAndShowStatistics();
    }
}