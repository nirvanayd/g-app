package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Profiles {
    LOCAL("local"),
    LOCAL_BLUE("local-blue"),
    LOCAL_GREEN("local-green"),
    PROD("prod"),
    PROD_BLUE("prod-blue"),
    PROD_GREEN("prod-green");

    private final String name;

    public static List<String> getList() {
        return Arrays.stream(Profiles.values()).map(Profiles::getName).collect(Collectors.toList());
    }

    public static List<String> getLocalList() {
        return Arrays.stream(Profiles.values()).map(Profiles::getName).
                filter(pName -> pName.startsWith("local")).collect(Collectors.toList());
    }

    public static List<String> getProdList() {
        return Arrays.stream(Profiles.values()).map(Profiles::getName).
                filter(pName -> pName.startsWith("prod")).collect(Collectors.toList());
    }

    public static String hasProfile(String env, String profile) {
        List<String> compare = null;
        if (env.equals("local")) {
            compare = getLocalList();
        } else if (env.equals("prod")) {
            compare = getProdList();
        }

        if (compare == null) return "not supported..";

        return compare.stream()
                .filter(c -> c.equals(profile))
                .findAny()
                .orElse("not supported..");
    }
}
