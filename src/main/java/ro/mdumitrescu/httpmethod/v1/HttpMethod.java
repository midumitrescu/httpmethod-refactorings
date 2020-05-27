package ro.mdumitrescu.httpmethod.v1;

import java.util.HashMap;
import java.util.Map;

/**
 *  <b>CONNECT</b> is a valid HTTP Method,
 *
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3">HTTP 1.1 RFC </a>
 */
public enum HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, CONNECT;

    private static final Map<String, HttpMethod> mappings = new HashMap<>(16);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    /**
     * Resolve the given method value to an {@code HttpMethod}.
     *
     * @param method the method value as a String
     * @return the corresponding {@code HttpMethod}, or {@code null} if not found
     * @since 4.2.4
     */
    public static HttpMethod resolve(String method) {
        return (method != null ? mappings.get(method) : null);
    }

    /**
     * Determine whether this {@code HttpMethod} matches the given
     * method value.
     *
     * @param method the method value as a String
     * @return {@code true} if it matches, {@code false} otherwise
     * @since 4.2.4
     */
    public boolean matches(String method) {
        return (this == resolve(method));
    }

}
