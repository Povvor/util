package util;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CoreLogicTest {

    @Test
    void defineStringTypeTest()  {
        CoreLogic coreLogic = new CoreLogic();
        Type resultString = coreLogic.defineStringType("Hello World!");
        Type resultInteger = coreLogic.defineStringType("42");
        Type resultFloat = coreLogic.defineStringType("3.14");
        assertThat(resultString).isEqualTo(Type.STRING);
        assertThat(resultInteger).isEqualTo(Type.INTEGER);
        assertThat(resultFloat).isEqualTo(Type.FLOAT);
    }

    @Test
    void initInputStringsWhenWrongInput() {
        CoreLogic coreLogic = new CoreLogic();
        String image = "src/test/java/resources/testImg.png";
        String wrongPath = "src/test/java/test.txt";
        String suitablFile = "src/test/java/resources/test.txt";
        coreLogic.getFiles().add(image);
        coreLogic.getFiles().add(wrongPath);
        coreLogic.getFiles().add(suitablFile);
        List<String> result = coreLogic.initInputStrings();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo("text");
    }

}