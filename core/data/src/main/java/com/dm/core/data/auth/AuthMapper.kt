package com.dm.core.data.auth

import com.dm.core.domain.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userId = this.userId
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userId = this.userId
    )
}