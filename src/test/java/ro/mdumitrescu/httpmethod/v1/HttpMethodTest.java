package ro.mdumitrescu.httpmethod.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpMethodTest {

    @Test
    public void introducingConnectIsNotABreakingChange() {

        HttpMethod connect = HttpMethod.CONNECT;

        switch (connect) {
            case GET: Assertions.fail();
            case HEAD:  Assertions.fail();
            default: // as expected
                break;
        }
    }

}