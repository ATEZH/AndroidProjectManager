package com.up.projectmanager.data.task

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Task(val name: String,
                    val description: String,
                    val createdOn: String,
                    val deadline: String,
                    val completed: Boolean,
                    var projectId: String) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readBoolean() ?: false,
        parcel.readString() ?: "",
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(createdOn)
        parcel.writeString(deadline)
        parcel.writeBoolean(completed)
        parcel.writeString(projectId)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Task> = object : Parcelable.Creator<Task> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun createFromParcel(parcel: Parcel): Task {
                return Task(parcel)
            }

            override fun newArray(size: Int): Array<Task?> {
                return arrayOfNulls(size)
            }
        }
    }
}