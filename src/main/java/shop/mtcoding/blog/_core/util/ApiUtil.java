package shop.mtcoding.blog._core.util;
import lombok.Data;

@Data
public class ApiUtil<T> { //제네릭 타입이 바뀔 수도 있어서 내가 new할때 타입을 결정할 수 있기 때문에 object를 쓰지않음
    private Integer status; //200, 400, 404, 405
    private String msg; //성공, 실패 시 -> 정확한 메시지
    private T body; //성공 시 줄 데이터

    public ApiUtil(T body) {
        this.status = 200;
        this.msg="성공";
        this.body = body;
    }

    public ApiUtil(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.body=null;
    }
}
