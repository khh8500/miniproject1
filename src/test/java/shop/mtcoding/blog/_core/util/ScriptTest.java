package shop.mtcoding.blog._core.util;

import org.junit.jupiter.api.Test;
import shop.mtcoding.blog._core.util.Script;

public class ScriptTest {
    @Test
    public void back_test(){
        String result = Script.back("권한이 없어요");
        System.out.println(result);
    }
}
