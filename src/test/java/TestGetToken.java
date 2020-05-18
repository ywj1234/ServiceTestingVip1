

import com.ktt.wework.Wework;
import com.ktt.wework.WeworkConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestGetToken {

    //@Test
    public void testToken(){
       Wework wework = new Wework();
       String token = wework.getWechatToken(WeworkConfig.getInstance().secret);
       assertThat(token,not(equalTo(null)));
    }
}
