package br.com.eventslist.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatTimestamp(timestamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val netDate = Date(timestamp)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}