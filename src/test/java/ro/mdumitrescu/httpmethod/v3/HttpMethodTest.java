package ro.mdumitrescu.httpmethod.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.mdumitrescu.httpmethod.v3.HttpMethod.*;
import static ro.mdumitrescu.httpmethod.v3.HttpMethod.Standard.GET;
import static ro.mdumitrescu.httpmethod.v3.HttpMethod.Standard.HEAD;

class HttpMethodTest {

    @Test
    public void resolvesNull() {
        assertThat(resolve(null)).isNull();
    }

    @ParameterizedTest(name = "{index} => {0} must resolve to {1}")
    @MethodSource("standardHttpMethodsProvider")
    public void resolvesHttpMethod(String name, HttpMethod correspondent) {

        Assertions.assertAll(
                () -> assertThat(resolve(name)).isEqualTo(correspondent),
                () -> assertThat(resolve(name)).isEqualTo(resolve(name)),
                () -> assertThat(resolve(name)).isNotEqualTo(resolve("RANDOM"))
        );
    }

    @Test
    public void resolvesNonStandardMethods() {
        assertThat(resolve("CONNECT")).isEqualTo(resolve("CONNECT"));
    }

    @Test
    public void equalsWorksProperly() {
        Assertions.assertAll(
                () -> assertThat(resolve("GET")).isEqualTo(resolve("GET")),
                () -> assertThat(resolve("GET")).isEqualTo(GET),
                () -> assertThat(resolve("GET")).isSameAs(GET),
                () -> assertThat(resolve("GET") == GET).withFailMessage("Just to be obvious. " +
                        "Now equals is symmetric").isTrue()
        );
    }

    @Test
    public void canEnumSet() {
        EnumSet.allOf(Standard.class);
    }

    @Test
    public void canWriteSwitchStatement() {
        Standard someHttpMethod = GET;
        switch (someHttpMethod) {
            case GET: break;
            default: Assertions.fail();
        }
    }

    private static Stream<Arguments> standardHttpMethodsProvider() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("GET", GET),
                Arguments.of("HEAD", HEAD),
                Arguments.of("POST", Standard.POST),
                Arguments.of("PUT", Standard.PUT),
                Arguments.of("PATCH", Standard.PATCH),
                Arguments.of("DELETE", Standard.DELETE),
                Arguments.of("OPTIONS", Standard.OPTIONS),
                Arguments.of("TRACE", Standard.TRACE)
        );
    }
}