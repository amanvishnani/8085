package com.amanvishnani.sim8085.domain;

/**
 * Enumeration of the 8085 CPU flags.
 */
public enum Flag {
    /** Sign flag: set if the result of an operation is negative. */
    S,
    /** Zero flag: set if the result of an operation is zero. */
    Z,
    /** Auxiliary Carry flag: set if there is a carry out of bit 3. */
    Ac,
    /** Parity flag: set if the number of set bits in the result is even. */
    P,
    /** Carry flag: set if there is a carry out of bit 7. */
    Cy;
}
