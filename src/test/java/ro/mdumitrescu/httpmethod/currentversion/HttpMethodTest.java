package ro.mdumitrescu.httpmethod.currentversion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpMethodTest {

    @Test
    public void resolvesNull() {
        assertThat(HttpMethod.resolve(null)).isNull();
    }

    @ParameterizedTest(name = "{index} => {0} must resolve to {1}")
    @MethodSource("standardHttpMethodsProvider")
    public void resolvesStandardMethods(String name, HttpMethod correspondent) {
        Assertions.assertAll(
                () -> assertThat(HttpMethod.resolve(name)).isEqualTo(correspondent),
                () -> assertThat(HttpMethod.resolve(name)).isSameAs(correspondent)
        );
    }


    private static Stream<Arguments> standardHttpMethodsProvider() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("GET", HttpMethod.GET),
                Arguments.of("HEAD", HttpMethod.HEAD),
                Arguments.of("POST", HttpMethod.POST),
                Arguments.of("PUT", HttpMethod.PUT),
                Arguments.of("PATCH", HttpMethod.PATCH),
                Arguments.of("DELETE", HttpMethod.DELETE),
                Arguments.of("OPTIONS", HttpMethod.OPTIONS),
                Arguments.of("TRACE", HttpMethod.TRACE)
        );
    }


}