package org.duckdns.ahamike.rollbook.config.constant;

public enum DataStatus {
    ACTIVE(100L),
    INACTIVE(0L);

    private final Long active;

    DataStatus(Long active) {
        this.active = active;
    }

    public Long getValue() {
        return active;
    }
}
