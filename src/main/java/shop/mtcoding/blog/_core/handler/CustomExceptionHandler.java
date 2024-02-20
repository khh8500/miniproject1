package shop.mtcoding.blog._core.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.Script;

@ControllerAdvice //응답에러 컨트롤러(view==파일 리턴) // 모든에러를 잡아챔
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class) //어떤에러가 발생할때 실행할지
    public @ResponseBody String error1(Exception e){//모든 에러를 받을 수 있음
        return Script.back(e.getMessage());
    }
}
