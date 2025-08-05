package util;

import lombok.Getter;

public class Main {
    private @Getter static final CoreLogic CORE_LOGIC = new CoreLogic();

    public static void main(String[] args) {
        CORE_LOGIC.run(args);
    }
}