package com.jokubas.mmdb.feedback_ui.error

import com.jokubas.mmdb.feedback_ui.R
import com.jokubas.mmdb.ui_kit.bindingclasses.ImageBind

sealed class GenericErrorViewModels {

    class NetworkErrorViewModel(
        onButtonClicked: () -> Unit
    ) : ErrorViewModel(
        icon = ImageBind.ByResource(R.drawable.ic_no_connection),
        title = "No connection",
        description = "There is no internet connection. Please reconnect and try again.",
        buttonConfig = ErrorButtonConfig(
            title = "Try again",
            onClicked = onButtonClicked
        )
    )

    object EmptyViewModel : ErrorViewModel(
        icon = ImageBind.ByResource(R.drawable.ic_nothing_found),
        title = "No data",
        description = "Not content was found by your request."
    )

    object Unknown : ErrorViewModel(
        icon = ImageBind.ByResource(R.drawable.ic_unknown_error),
        title = "Something went wrong...",
        description = "Unknown failure occurred while handling your request."
    )
}
