// Generated by delombok at Thu Apr 12 13:54:15 CEST 2018
package com.dokany.java.structure;

import java.util.AbstractSet;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;

import com.dokany.java.constants.DokanOption;
import com.dokany.java.constants.EnumInteger;

/**
 * Used to store multiple enum values such as {@link com.dokany.java.constants.FileSystemFeature} and {@link DokanOption}.
 *
 * @param <T> Type of enum
 */
public final class EnumIntegerSet<T extends Enum<T> & EnumInteger> extends AbstractSet<T> {
	private final EnumSet<T> elements;

	public EnumIntegerSet(final Class<T> clazz) {
		elements = EnumSet.noneOf(clazz);
	}

	public EnumIntegerSet(T first, T... others) {
		this.elements = EnumSet.of(first, others);
	}

	@SafeVarargs
	public final void add(final T... items) {
		if (Objects.isNull(items) || (items.length < 1)) {
			throw new IllegalArgumentException("items array cannot be empty");
		}
		for (final T item : items) {
			if (Objects.nonNull(item)) {
				elements.add(item);
			}
		}
	}

	public int toInt() {
		int toReturn = 0;
		for (final T current : elements) {
			// Already checked (in constructor) to ensure only objects which implement EnumInteger are stored in values
			final EnumInteger enumInt = (EnumInteger) current;
			toReturn |= enumInt.getMask();
		}
		return toReturn;
	}

	@Override
	public boolean add(final T e) {
		return elements.add(e);
	}

	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	@SuppressWarnings("all")
	public String toString() {
		return "EnumIntegerSet(elements=" + this.elements + ")";
	}
}
