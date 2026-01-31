package com.amanvishnani.sim8085.domain;

import java.util.Set;

/**
 * Interface for managing CPU flags.
 */
public interface IFlags {
    /**
     * Sets a specific flag to the given value.
     * 
     * @param flagName The flag to set.
     * @param value    The value (usually 0 or 1).
     */
    void setFlag(Flag flagName, Integer value);

    /**
     * Gets the value of a specific flag.
     * 
     * @param flagName The flag to get.
     * @return The flag value.
     */
    Integer getFlag(Flag flagName);

    /**
     * Returns the set of all available flags.
     * 
     * @return Set of Flags.
     */
    Set<Flag> getKeys();

    /**
     * Resets all flags to zero.
     */
    void fillZeros();
}
