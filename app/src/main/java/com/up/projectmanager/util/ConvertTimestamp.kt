package com.up.projectmanager.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ConvertTimestamp {
    fun timestampToString(firebaseTimestamp: Any?): String {
        if (firebaseTimestamp is Timestamp) {
            val date = firebaseTimestamp.toDate()
            val format = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.getDefault())
            return format.format(date)
        }
        return "No Deadline"
    }
}