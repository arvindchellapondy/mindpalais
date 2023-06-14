package com.mindpalais.android.model

import com.google.gson.annotations.SerializedName

// Define the response model
data class ResponseModel(
    var id: String,
    var created: String,
    val message: String,
    val action: String?,
    val people: Array<String>,
    var status: String?,
    @SerializedName("date") val date: String?,
//    val recurringDates: Array<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResponseModel

        if (created != other.created) return false
        if (message != other.message) return false
        if (action != other.action) return false
        if (!people.contentEquals(other.people)) return false
        if (status != other.status) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = created.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + (action?.hashCode() ?: 0)
        result = 31 * result + people.contentHashCode()
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }
}

object TaskStatus {
    const val READY = "Ready"
    const val IN_PROGRESS = "In Progress"
    const val BLOCKED = "Blocked"
    const val COMPLETE = "Complete"
}