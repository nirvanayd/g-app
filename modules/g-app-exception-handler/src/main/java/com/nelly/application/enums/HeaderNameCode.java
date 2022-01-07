package com.nelly.application.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HeaderNameCode {
    AUTHORIZATION("Authorization");

    private String name;
    public String getName() {
        return this.name;
    }
}
