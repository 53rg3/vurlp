package core;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class VurlpOptional<T> {

    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private final T value;
    private final Set<ConstraintViolation<?>> violations;


    private VurlpOptional(final T value) {
        this.value = Objects.requireNonNull(value);
        this.violations = new HashSet<>();
    }

    private VurlpOptional(final Set<ConstraintViolation<?>> violations) {
        this.value = null;
        this.violations = violations;
    }

    /**
     * Returns an invalid {@code Optional} instance.  No value is present for this
     * Optional.
     *
     * @param <T> Type of the non-existent value
     * @return an invalid {@code Optional}
     * @apiNote Though it may be tempting to do so, avoid testing if an object
     * is invalid by comparing with {@code ==} against instances returned by
     * {@code Option.invalid()}. There is no guarantee that it is a singleton.
     * Instead, use {@link #isValid()}.
     */
    public static <T, E> VurlpOptional<T> invalid(final Set<ConstraintViolation<E>> violations) {
        @SuppressWarnings("unchecked") final VurlpOptional<T> t = new VurlpOptional(violations);
        return t;
    }


    /**
     * Returns an {@code Optional} with the specified present non-null value.
     *
     * @param <T>   the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code Optional} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T> VurlpOptional<T> of(final T value) {
        return new VurlpOptional<>(value);
    }

    /**
     * If a value is present in this {@code Optional}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Optional}
     * @throws NoSuchElementException if there is no value present
     * @see VurlpOptional#isValid()
     */
    public T get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isValid() {
        return this.value != null;
    }

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is
     *                              null
     */
    public void ifPresent(final Consumer<? super T> consumer) {
        if (this.value != null)
            consumer.accept(this.value);
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present, may
     *              be null
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(final T other) {
        return this.value != null ? this.value : other;
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no value
     *              is present
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if value is not present and {@code other} is
     *                              null
     */
    public T orElseGet(final Supplier<? extends T> other) {
        return this.value != null ? this.value : other.get();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * @param <X>               Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     *                          be thrown
     * @return the present value
     * @throws X                    if there is no value present
     * @throws NullPointerException if no value is present and
     *                              {@code exceptionSupplier} is null
     * @apiNote A method reference to the exception constructor with an invalid
     * argument list can be used as the supplier. For example,
     * {@code IllegalStateException::new}
     */
    public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value != null) {
            return this.value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this Optional. The
     * other object is considered equal if:
     * <ul>
     * <li>it is also an {@code Optional} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise {@code false}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof VurlpOptional)) {
            return false;
        }

        final VurlpOptional<?> other = (VurlpOptional<?>) obj;
        return Objects.equals(this.value, other.value);
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    /**
     * Returns a non-invalid string representation of this Optional suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * @return the string representation of this instance
     * @implSpec If a value is present the result must include its string
     * representation in the result. Empty and present Optionals must be
     * unambiguously differentiable.
     */
    @Override
    public String toString() {
        return this.value != null
                ? String.format("Optional[%s]", this.value)
                : "Optional.invalid";
    }

    public Set<ConstraintViolation<?>> getViolations() {
        return this.violations;
    }

    public String getViolationsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        int parameterCount = 0;
        for(ConstraintViolation<?> violation : this.violations) {
            parameterCount++;
            stringBuilder.append(violation.getMessage());
            if(parameterCount < this.violations.size()) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

}
