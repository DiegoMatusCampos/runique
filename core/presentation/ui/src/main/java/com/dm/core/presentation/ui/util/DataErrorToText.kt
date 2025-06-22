package com.dm.core.presentation.ui.util

import com.dm.core.domain.util.DataError
import com.dm.core.presentation.ui.R
import com.dm.core.presentation.ui.util.UiText.*

fun DataError.asUiText(): UiText {
    return when(this){
        DataError.Local.DISK_FULL -> StringResource(
            R.string.error_disk_full
        )
        DataError.Network.REQUEST_TIMEOUT -> StringResource(
            R.string.error_request_timeout
        )
        DataError.Network.TOO_MANY_REQUESTS -> StringResource(
            R.string.error_too_many_requests
        )
        DataError.Network.NO_INTERNET -> StringResource(
            R.string.error_no_internet
        )
        DataError.Network.PAYLOAD_TOO_LARGE -> StringResource(
            R.string.error_payload_too_large
        )
        DataError.Network.SERVER_ERROR -> StringResource(
            R.string.error_server_error
        )
        DataError.Network.SERIALIZATION -> StringResource(
            R.string.error_serialization
        )
        else -> StringResource(
            R.string.error_unknow
        )
    }
}