package com.zerobase.reservation.user.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Auth {
    ROLE_USER("user"),
    ROLE_PARTNER("partner");

    private final String authority;

    public String getAuthority() {
        return authority;
    }
}
