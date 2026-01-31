package com.amanvishnani.sim8085.domain;

import java.util.Optional;

/**
 * Interface representing an operand in an 8085 instruction.
 * Operands can be labels or memory addresses.
 */
public interface IOperand {
    /** @return Optional label name if the operand is a label. */
    Optional<String> getLabel();

    /** @return Optional address if the operand is a memory address. */
    Optional<IAddress> getAddress();
}
