package com.nelly.application.exception.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HeaderNameCode {
    AUTHORIZATION("Authorization");

    private String name;
    public String getName() {
        return this.name;
    }
}
