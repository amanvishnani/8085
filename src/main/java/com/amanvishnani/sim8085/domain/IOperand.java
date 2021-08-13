package com.amanvishnani.sim8085.domain;

import java.util.Optional;

public interface IOperand {
    Optional<String> getLabel();
    Optional<IAddress> getAddress();
}
