package vn.ochabot.seaconnect.core.exception

import vn.ochabot.seaconnect.R

/**
 * Handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure(val errorCode: Int = 0) {
    open class GeneralFailure(val msg: Int) : Failure()

    class ExceptionError(val e: Throwable) : Failure()

    class NetworkError : GeneralFailure(R.string.error_network_connection)

    abstract class FeatureFailure : Failure()
}
