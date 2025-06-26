package com.dm.core.data.auth

import android.content.SharedPreferences
import com.dm.core.domain.AuthInfo
import com.dm.core.domain.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences
): SessionStorage {
    override suspend fun get(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(authInfo: AuthInfo?) {
        withContext(Dispatchers.IO) {
            if(authInfo == null){
                sharedPreferences.edit(commit = true) { remove(KEY_AUTH_INFO) }
                return@withContext
            }

            val json = Json.encodeToString(authInfo.toAuthInfoSerializable())
            sharedPreferences
                .edit(commit = true) {
                    putString(KEY_AUTH_INFO, json)
                }

        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}