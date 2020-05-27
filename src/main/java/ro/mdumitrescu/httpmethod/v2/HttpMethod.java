package ro.mdumitrescu.httpmethod.v2;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface HttpMethod {

    HttpMethod GET = HttpMethods.GET;
    HttpMethod HEAD = HttpMethods.HEAD;
    HttpMethod POST = HttpMethods.POST;
    HttpMethod PUT = HttpMethods.PUT;
    HttpMethod PATCH = HttpMethods.PATCH;
    HttpMethod DELETE = HttpMethods.DELETE;
    HttpMethod OPTIONS = HttpMethods.OPTIONS;
    HttpMethod TRACE = HttpMethods.TRACE;

    /**
     * Resolve the given method value to an {@code HttpMethod}.
     *
     * @param method the method value as a String
     * @return {@code null} if the method is an empty String, the corresponding {@code HttpMethods} if any found or a new
     * {@code NonStandardHttpMethod}
     * @since 4.2.4
     */
    static HttpMethod resolve(String method) {

        if (StringUtils.isEmpty(method)) {
            return null;
        }

        return Optional.ofNullable(method)
                .<HttpMethod>map(HttpMethods::resolve)
                .orElseGet(() ->
                        NonStandardHttpMethod.resolve(method)
                );
    }

    /**
     * Determine whether this {@code HttpMethod} matches the given
     * method value.
     *
     * @param method the method value as a String
     * @return {@code true} if it matches, {@code false} otherwise
     * @since 4.2.4
     */
    boolean matches(String method);

    String name();
}
