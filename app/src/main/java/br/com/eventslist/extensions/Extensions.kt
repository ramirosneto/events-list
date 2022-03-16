package br.com.eventslist.extensions

import br.com.eventslist.utils.NetworkStatus
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.handleThrowable(): NetworkStatus.Error {
    return if (this is UnknownHostException) {
        NetworkStatus.Error(errorMessage = "Verifique sua conexão com a internet e tente novamente.")
    } else if (this is HttpException && this.code() == 403) {
        NetworkStatus.Error(errorMessage = "Erro de autenticação. Tente novamente mais tarde.")
    } else if (this is SocketTimeoutException) {
        NetworkStatus.Error(errorMessage = "Verifique sua conexão com a internet e tente novamente.")
    } else if (this.message.isNullOrEmpty().not()) {
        NetworkStatus.Error(errorMessage = this.message ?: "")
    } else {
        NetworkStatus.Error(errorMessage = "Erro desconhecido. Tente novamente mais tarde.")
    }
}