package de.nimel;

/**
 * minimal complex number representation
 * 
 * <p>
 * This implementation is immutable
 * </p>
 */
final class ComplexNumber {

	private final double real;

	private final double imaginary;

	public ComplexNumber(final double a, final double b) {
		this.real = a;
		this.imaginary = b;
	}

	@Override
	public boolean equals(final Object other) {

		if (!(other instanceof ComplexNumber)) {
			return false;
		}

		ComplexNumber aux = (ComplexNumber) other;

		return aux.real == real && aux.imaginary == imaginary;
	}

	@Override
	public int hashCode() {

		int hash = 17;

		hash *= 31;
		hash += real;
		hash *= 31;
		hash += imaginary;

		return hash;
	}

	@Override
	public String toString() {
		return real + "+" + imaginary + "i";
	}
}