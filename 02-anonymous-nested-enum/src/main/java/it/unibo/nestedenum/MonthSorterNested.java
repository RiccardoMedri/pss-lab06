package it.unibo.nestedenum;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * Implementation of {@link MonthSorter}.
 */
public final class MonthSorterNested implements MonthSorter {

    private enum Month {
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);

        private final int days;

        Month(final int days) {
            this.days = days;
        }

        int getDays() {
            return this.days;
        }

        static Month fromString(final String input) {
            final String normalized = Objects.requireNonNull(input).toLowerCase(Locale.ROOT);
            Month result = null;
            for (final Month month : values()) {
                if (month.name().toLowerCase(Locale.ROOT).startsWith(normalized)) {
                    if (result != null) {
                        throw new IllegalArgumentException("Ambiguous month: " + input);
                    }
                    result = month;
                }
            }
            if (result == null) {
                throw new IllegalArgumentException("Invalid month: " + input);
            }
            return result;
        }
    }

    private static final class SortByMonthOrder implements Comparator<String> {
        @Override
        public int compare(final String first, final String second) {
            return Integer.compare(Month.fromString(first).ordinal(), Month.fromString(second).ordinal());
        }
    }

    private static final class SortByDate implements Comparator<String> {
        @Override
        public int compare(final String first, final String second) {
            return Integer.compare(Month.fromString(first).getDays(), Month.fromString(second).getDays());
        }
    }

    @Override
    public Comparator<String> sortByDays() {
        return new SortByDate();
    }

    @Override
    public Comparator<String> sortByOrder() {
        return new SortByMonthOrder();
    }
}
