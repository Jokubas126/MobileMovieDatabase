package com.jokubas.mmdb.feedback_ui.error

sealed class GenericErrorViewModels {

    class NetworkErrorViewModel(
        onButtonClicked: () -> Unit
    ): ErrorViewModel(
        title = "No connection",
        description = "There is no internet connection. Please reconnect and try again",
        buttonConfig = ErrorButtonConfig(
            title = "Try again",
            onClicked = onButtonClicked
        )
    )

    object EmptyViewModel: ErrorViewModel(
        title = "Nothing found",
        description = "No content was found"
    )
}
