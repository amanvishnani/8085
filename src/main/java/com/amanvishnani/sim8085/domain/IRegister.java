package com.amanvishnani.sim8085.domain;

/**
 * Interface representing a CPU register.
 * Extends IData as registers hold data bytes.
 */
public interface IRegister extends IData {
    /**
     * Updates the register content with new data.
     * 
     * @param data The new data.
     */
    void update(IData data);
}
