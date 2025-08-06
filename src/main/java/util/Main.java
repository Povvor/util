package util;

import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            PrintUtils.printNoArgsMsg();
            return;
        }
        Options options = parseArgs(args);
        CoreLogic coreLogic = new CoreLogic(options);
        coreLogic.run();
    }

    public static Options parseArgs(String[] args) {
        boolean isShortStat = false;
        boolean isFullStat = false;
        StandardOpenOption toRewrite = StandardOpenOption.TRUNCATE_EXISTING;
        String prefix = "";
        String path = "";
        List<String> files = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    isShortStat = true;
                    break;
                case "-f":
                    isFullStat = true;
                    break;
                case "-a":
                    toRewrite = StandardOpenOption.APPEND;
                    break;
                case "-p":
                    prefix = findValueAfterArg(args, i);
                    i++;
                    break;
                case "-o":
                    path = (findValueAfterArg(args, i) + '/');
                    i++;
                    break;
                case "-h", "--help":
                    PrintUtils.printHelp();
                    break;
                default:
                    files.add(args[i]);
                    break;
            }
        }
        return new Options(isShortStat, isFullStat, files, toRewrite, prefix, path);
    }

    public static String findValueAfterArg(String[] args, int index) {
        try {
            return args[index + 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка при обработке аргумента после опции: " + args[index]);
        }
        return "";
    }
}