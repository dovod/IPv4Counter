package org.example;

import java.util.Objects;
import java.util.function.ToLongFunction;

public class Hll32Counter<E> implements UniqueCounter<E> {
    private static final int DEFAULT_PRECISION = 20;
    private final byte[] register;
    private final ToLongFunction<E> calcHash;
    private final int registerLen;
    private double alpha = 0.0;

    public Hll32Counter() {
        this(DEFAULT_PRECISION);
    }

    public Hll32Counter(int precision) {
        this(precision, E::hashCode);
    }

    public Hll32Counter(int precision, ToLongFunction<E> calcHash) {
        validatePrecision(precision);
        this.registerLen = 1 << precision;
        this.register = new byte[registerLen];
        this.calcHash = Objects.requireNonNullElseGet(calcHash, () -> this::hashCode);

    }

    @Override
    public void add(E element) {
        final long hash = calcHash.applyAsLong(element);
        final int index = calcIndex(hash);
        final byte rank = calcRank(hash);
        register[index] = (byte) Math.max(register[index], rank);
    }

    @Override
    public int getUniqueCount() {
        return cardinality();
    }

    private void validatePrecision(int precision) {
        if (precision < 15 || precision > 28) {
            throw new RuntimeException("Precision must be 15 < p < 28");
        }
    }

    private int calcIndex(long hash) {
        return (int) (hash & (registerLen - 1));
    }

    private byte calcRank(long hash) {
        return (byte) (Long.numberOfLeadingZeros(hash) + 1);
    }

    private int cardinality() {
        if (alpha == 0.0) {
            calcAlpha();
        }

        double sum = 0;
        for (byte i : register) {
            sum += 1.0 / (1 << i);
        }

        return (int) (alpha * registerLen * registerLen * (1.0 / sum));
    }

    private void calcAlpha() {
        alpha = 0.7213 / (1 + 1.079 / registerLen);
    }

    private long hashCode(E element) {
//        Даёт хорошую точность, но по условию задачи можно использовать только стандартные библиотеки java
//        if (element instanceof String) {
//            return MurmurHash3.hash64(((String) element).getBytes());
//        }
        long result = Objects.hash(element);
        result = 31 * result;
        return result;
    }

}