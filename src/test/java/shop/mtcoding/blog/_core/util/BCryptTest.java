package shop.mtcoding.blog._core.util;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {
    @Test
    public void checkpw(){
        System.out.println((BCrypt.checkpw("1234", "$2a$10$fZjd/L.hpXphkc7BngLTXOvTMWN/3YueZFg9kMEchwwmIkiBRQ0jC")));
    }

    @Test
    public void gensal_test(){
        String salt = BCrypt.gensalt();
        System.out.println(salt);
    }

    //$2a$10$fZjd/L.hpXphkc7BngLTXOvTMWN/3YueZFg9kMEchwwmIkiBRQ0jC
    //$2a$10$onodzfz6AqdPzQ1FwSmDuuaaBYALTAzJuyA7mc5UyZ9h8jkVlKg2.
    @Test
    public void haspw_test(){
        String rawPassword="1234";
        String encPassword= BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        System.out.println(encPassword);
    }
}
