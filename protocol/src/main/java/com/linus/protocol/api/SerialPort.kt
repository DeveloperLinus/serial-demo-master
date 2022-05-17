package com.linus.protocol.api

import com.linus.common.utils.log
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream

class SerialPort {
    private var mFd: FileDescriptor? = null
    private var mFileInputStream: FileInputStream? = null
    private var mFileOutputStream: FileOutputStream? = null
    fun open(device: File, baudrate: Int, flags: Int): Boolean {
        mFd = Companion.open(device.getAbsolutePath(), baudrate, flags)
        if (mFd == null) {
            log( "native open returns null")
            return false
        }
        mFileInputStream = FileInputStream(mFd)
        if (mFileInputStream == null) {
            return false
        }
        mFileOutputStream = FileOutputStream(mFd)
        return mFileOutputStream != null
    }

    // Getters and setters
    val inputStream: java.io.InputStream?
        get() = if (mFd != null) {
            mFileInputStream
        } else {
            null
        }

    val outputStream: java.io.OutputStream?
        get() {
            return if (mFd != null) {
                mFileOutputStream
            } else {
                null
            }
        }

    fun release() {
        try {
            if (null != mFileOutputStream) {
                mFileOutputStream!!.close()
                mFileOutputStream = null
            }
            if (null != mFileInputStream) {
                mFileInputStream!!.close()
                mFileInputStream = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        close()
    }

    external fun close()

    companion object {
        private const val TAG = "SerialPort"

        // JNI
        private external fun open(path: String, baudrate: Int, flags: Int): FileDescriptor?

        init {
            System.loadLibrary("serial_port")
        }
    }
}