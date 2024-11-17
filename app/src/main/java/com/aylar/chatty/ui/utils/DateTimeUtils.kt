package com.aylar.chatty.ui.utils


import android.os.CountDownTimer
import java.util.Calendar
import java.util.concurrent.TimeUnit


object TimerCreator {
    fun createCountDownTimer(
        milliseconds: Int,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ): CountDownTimer {
        val time = Calendar.getInstance()
        time.add(Calendar.MILLISECOND, milliseconds)
        val timeInMillis = time.timeInMillis

        val timer =
            object : CountDownTimer(timeInMillis - Calendar.getInstance().timeInMillis, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    onTick(millisUntilFinished)
                }

                override fun onFinish() {
                    onFinish()
                }
            }
        return timer
    }
}

fun getFormattedCountTime(ms: Long, includeMillis: Boolean = false): String {
    var milliseconds = ms
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    milliseconds -= TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
    if (!includeMillis) {
        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }
    milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
    milliseconds /= 10
    return "${if (hours < 10) "0" else ""}$hours:" +
            "${if (minutes < 10) "0" else ""}$minutes:" +
            "${if (seconds < 10) "0" else ""}$seconds:" +
            "${if (milliseconds < 10) "0" else ""}$milliseconds"
}

fun getFormattedCountTimeShort(ms: Long, includeMillis: Boolean = false): String {
    var milliseconds = ms
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    milliseconds -= TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
    if (!includeMillis) {
        return "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }
    milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
    milliseconds /= 10
    return "${if (minutes < 10) "0" else ""}$minutes:" +
            "${if (seconds < 10) "0" else ""}$seconds"
}


