package com.up.projectmanager.data.project

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.up.projectmanager.data.task.Task

data class Project(val id: String,
                val name: String,
                val description: String,
                val createdOn: String,
                val deadline: String,
                val members: List<String>,
                var tasks: List<Task>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: arrayListOf(),
        parcel.createTypedArrayList(Task.CREATOR) ?: arrayListOf()
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(createdOn)
        parcel.writeString(deadline)
        parcel.writeStringList(members)
        parcel.writeParcelableList(tasks, flags)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Project> = object : Parcelable.Creator<Project> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun createFromParcel(parcel: Parcel): Project {
                return Project(parcel)
            }

            override fun newArray(size: Int): Array<Project?> {
                return arrayOfNulls(size)
            }
        }
    }
}