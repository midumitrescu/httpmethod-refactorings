package ro.mdumitrescu.httpmethod.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpMethodTest {

    @Test
    public void resolvesNull() {
        assertThat(HttpMethod.resolve((String) null)).isNull();
    }

    @ParameterizedTest(name = "{index} => {0} must resolve to {1}")
    @MethodSource("standardHttpMethodsProvider")
    public void resolvesHttpMethod(String name, HttpMethod correspondent) {

        Assertions.assertAll(
                () -> assertThat(HttpMethod.resolve(name)).isEqualTo(correspondent),
                () -> assertThat(HttpMethod.resolve(name)).isEqualTo(HttpMethod.resolve(name)),
                () -> assertThat(HttpMethods.resolve(name)).isEqualTo(correspondent)
        );
    }

    @Test
    public void resolvesNonStandardMethods() {
        assertThat(HttpMethod.resolve("CONNECT")).isEqualTo(NonStandardHttpMethod.resolve("CONNECT"));
    }

    @Test
    public void equalsWorksProperly() {
        Assertions.assertAll(
                () -> assertThat(HttpMethod.resolve("GET")).isEqualTo(HttpMethod.resolve("GET")),
                () -> assertThat(HttpMethod.resolve("CONNECT")).isEqualTo(HttpMethod.resolve("CONNECT")),
                () -> assertThat(NonStandardHttpMethod.resolve("GET")).isEqualTo(HttpMethod.resolve("GET")),
                () -> assertThat(NonStandardHttpMethod.resolve("GET")).isEqualTo(HttpMethods.GET)
        );
    }

    @Test
    @Disabled("obviously buggy")
    public void equalsUnfortunatelyDoesWorksProperly() {

        Assertions.assertAll(
                () -> assertThat(NonStandardHttpMethod.resolve("GET").equals(HttpMethod.GET))
                        .withFailMessage("Expecting equals to be symmetric").isTrue(),
                () -> assertThat(HttpMethod.GET == NonStandardHttpMethod.resolve("GET"))
                        .withFailMessage("Expecting equals to be symmetric").isTrue(),
                () -> assertThat(HttpMethod.GET.equals(NonStandardHttpMethod.resolve("GET")))
                        .withFailMessage("Expecting equals to be symmetric").isTrue()
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