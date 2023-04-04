package com.suntech.colorcall.helper

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.suntech.colorcall.model.Contact
import javax.inject.Inject


class ContactHelper @Inject constructor(val context: Context) {
    fun getAllContact(): MutableList<Contact> {
        val contactList: MutableList<Contact> = mutableListOf()
        val cur = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        cur?.apply {
            while (this.moveToNext()) {
                val id = getLong(getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: getString(
                    getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
                    )
                )
                val contactUri: Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id)
                val avatar: Uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
                val contact = Contact(id, name, phoneNumber, avatar.toString())
                contactList.add(contact)
            }
        }?.close()
        return contactList
    }

/*    fun getIdByNumber(phoneNumber: String): Int? {
        val contentResolver = context.contentResolver
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup._ID)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.apply {
            while (moveToNext()) {
                return getInt(getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
            }

        }?.close()
        return null
    }*/

    fun getNameByNumber(phoneNumber: String): String? {
        val contentResolver = context.contentResolver
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.apply {
            while (moveToNext()) {
                return getString(getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }?.close()
        return null
    }
}
