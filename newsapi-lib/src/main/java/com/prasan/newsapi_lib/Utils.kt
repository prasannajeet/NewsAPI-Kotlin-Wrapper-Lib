package com.prasan.newsapi_lib

import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException

/**
 * Readable naming convention for Network call lambda
 * @since 1.0
 */
typealias NetworkAPIInvoke<T> = suspend () -> Response<T>

/**
 * typealias for lambda passed when a photo is tapped on in Popular Photos Fragment
 */
typealias ListItemClickListener<T> = (T) -> Unit

/**
 * Utility function that works to perform a Retrofit API call and return either a success model
 * instance or an error message wrapped in an [Exception] class
 * @param messageInCaseOfError Custom error message to wrap around [State.Failure]
 * with a default value provided for flexibility
 * @param networkApiCall lambda representing a suspend function for the Retrofit API call
 * @return [State.Success] object of type [T], where [T] is the success object wrapped around
 * [State.Success] if network call is executed successfully, or [State.Failure]
 * object wrapping an [Exception] class stating the error
 * @since 1.0
 */
@ExperimentalCoroutinesApi
internal suspend fun <T : Any> performSafeNetworkApiCall(
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    networkApiCall: NetworkAPIInvoke<T>
): Flow<State<T>> {
    var delayDuration = 1000L
    val delayFactor = 2
    return flow {
        val response = networkApiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(State.Success(it))
            }
                ?: emit(State.Failure(IOException("API call successful but empty response body")))
            return@flow
        }
        emit(
            State.Failure(
                IOException(
                    "API call failed with error - ${response.errorBody()
                        ?.string() ?: messageInCaseOfError}"
                )
            )
        )
        return@flow
    }.catch { e ->
        emit(State.Failure(IOException("Exception during network API call: ${e.message}")))
        return@catch
    }.retryWhen { cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.flowOn(Dispatchers.IO)
}

/**
 * Lets the UI act on a controlled bound of states that can be defined here
 * @author Prasan
 * @since 1.0
 */
internal sealed class State<out T : Any> {

    /**
     * Represents UI state where the UI should be showing a loading UX to the user
     * @param isLoading will be true when the loading UX needs to display, false when not
     */
    data class Loading(val isLoading: Boolean) : State<Nothing>()

    /**
     * Represents the UI state where the operation requested by the UI has been completed successfully
     * and the output of type [T] as asked by the UI has been provided to it
     * @param output result object of [T] type representing the fruit of the successful operation
     */
    data class Success<out T : Any>(val output: T) : State<T>()

    /**
     * Represents the UI state where the operation requested by the UI has failed to complete
     * either due to a IO issue or a service exception and the same is conveyed back to the UI
     * to be shown the user
     * @param throwable [Throwable] instance containing the root cause of the failure in a [String]
     */
    data class Failure(val throwable: Throwable) : State<Nothing>()
}

/**
 * Extension function on a fragment to show a toast message
 */
fun Context.showToast(@NonNull message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}