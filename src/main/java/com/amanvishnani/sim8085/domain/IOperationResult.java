package com.amanvishnani.sim8085.domain;

/**
 * Interface for the result of a data operation.
 * Contains both the resulting data and the updated flags.
 */
public interface IOperationResult {
    /**
     * Returns the data resulting from the operation.
     * 
     * @return The resulting data byte.
     */
    IData getData();

    /**
     * Returns the CPU flags as they were after the operation.
     * 
     * @return The CPU flags after the operation.
     */
    IFlags getFlags();
}
