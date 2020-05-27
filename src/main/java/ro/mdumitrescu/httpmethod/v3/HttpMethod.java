package ro.mdumitrescu.httpmethod.v3;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface HttpMethod {

    enum Standard implements HttpMethod {
        GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

        private static final Map<String, HttpMethod.Standard> mappings = new HashMap<>(16);

        static {
            for (HttpMethod.Standard httpMethod : values()) {
                mappings.put(httpMethod.name(), httpMethod);
            }
        }
    }

    /**
     * Resolve the given method value to an {@code HttpMethod}.
     *
     * @param method the method value as a String
     * @return the corresponding {@code HttpMethod}, or {@code null} if not found
     * @since 4.2.4
     */
    static HttpMethod resolve(String method) {

        if (StringUtils.isEmpty(method)) {
            return null;
        }

        HttpMethod proposal = Standard.mappings.get(method);

        return proposal == null ? NonStandardHttpMethod.of(method) : proposal;
    }

    @NonNull
    String name();

    static HttpMethod[] values() {
        return Standard.values();
    }

    /**
     * Determine whether this {@code HttpMethod} matches the given
     * method value.
     *
     * @param method the method value as a String
     * @return {@code true} if it matches, {@code false} otherwise
     * @since 4.2.4
     */
    default boolean matches(String method) {
        if(StringUtils.isEmpty(method)) {
            return false;
        }
        return (this.name()
                .equals(
                        Objects.requireNonNull(resolve(method)).name()));
    }

    final class NonStandardHttpMethod implements HttpMethod {
        @NonNull
        private final String method;

        private NonStandardHttpMethod(@NonNull String method) {
            if (StringUtils.isEmpty(method)) {
                throw new IllegalArgumentException(null + " is not allowed as HttpMethod value");
            }
            this.method = preferUppercase(method);
        }

        private String preferUppercase(String method) {
            return method.toUpperCase();
        }

        private static NonStandardHttpMethod of(@NonNull String method) {
            return new NonStandardHttpMethod(method);
        }

        public String name() {
            return method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HttpMethod)) return false;
            HttpMethod that = (HttpMethod) o;
            return this.matches(that.name());
        }

        @Override
        public int hashCode() {
            return Objects.hash(method);
        }
    }
}
