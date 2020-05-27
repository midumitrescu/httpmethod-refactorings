package ro.mdumitrescu.httpmethod.v2;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Adds extensibility for non standard Http Methods.
 */
public class NonStandardHttpMethod implements HttpMethod {

    @NonNull
	private final String method;

	public NonStandardHttpMethod(String method) {
		if(StringUtils.isEmpty(method)) {
			throw new IllegalArgumentException(null + " is not allowed as HttpMethod value");
		}
		this.method = preferUppercase(method);
	}

    private String preferUppercase(String method) {
        return method.toUpperCase();
    }

    @Nullable
	public static NonStandardHttpMethod resolve(@Nullable String method) {
	    if(StringUtils.isEmpty(method)) {
	        return null;
        }
		return new NonStandardHttpMethod(method);
	}

	/**
	 * Determine whether this {@code HttpMethod} matches the given
	 * method value.
	 * @param method the method value as a String
	 * @return {@code true} if it matches, {@code false} otherwise
	 * @since 4.2.4
	 */
	public boolean matches(String method) {
		return (this.method.equals(method));
	}

	@Override
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
