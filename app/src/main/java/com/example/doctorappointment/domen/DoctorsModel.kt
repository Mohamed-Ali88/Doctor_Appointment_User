package com.example.doctorappointment.domen

import android.os.Parcel
import android.os.Parcelable

data class DoctorsModel(
    val Address: String? = null,
    val Biography: String? = null,
    val Id: Int? = null,
    val Name: String? = null,
    val Picture: String? = null,
    val Special: String? = null,
    val Expriense: Int? = null,
    val Location: String? = null,
    val Mobile: String? = null,
    val Patiens: String? = null,
    val Rating: Double? = null,
    val Site: String? = null,
    val TopDoctor : Boolean? =null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Address)
        parcel.writeString(Biography)
        parcel.writeValue(Id)
        parcel.writeString(Name)
        parcel.writeString(Picture)
        parcel.writeString(Special)
        parcel.writeValue(Expriense)
        parcel.writeString(Location)
        parcel.writeString(Mobile)
        parcel.writeString(Patiens)
        parcel.writeValue(Rating)
        parcel.writeString(Site)
        parcel.writeValue(TopDoctor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DoctorsModel> {
        override fun createFromParcel(parcel: Parcel): DoctorsModel {
            return DoctorsModel(parcel)
        }

        override fun newArray(size: Int): Array<DoctorsModel?> {
            return arrayOfNulls(size)
        }
    }
}
