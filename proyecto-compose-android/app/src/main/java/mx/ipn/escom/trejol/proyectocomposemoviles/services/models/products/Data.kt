package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products

import android.os.Parcelable
import android.os.Parcel

data class Data(
    val category_id: Int,
    val created_at: Any?,
    val description: String,
    val id: Int,
    var name: String,
    val price: Int,
    val status: Int,
    val updated_at: Any?,
    val user_create_id: Int,
    val user_delete_id: Any?,
    val user_update_id: Any?,
    var uuid: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Any::class.java.classLoader),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Any::class.java.classLoader),
        parcel.readInt(),
        parcel.readValue(Any::class.java.classLoader),
        parcel.readValue(Any::class.java.classLoader),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(category_id)
        parcel.writeValue(created_at)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeInt(status)
        parcel.writeValue(updated_at)
        parcel.writeInt(user_create_id)
        parcel.writeValue(user_delete_id)
        parcel.writeValue(user_update_id)
        parcel.writeString(uuid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}

