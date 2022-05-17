package com.linus.protocol.net

import com.linus.common.utils.Tools
import java.nio.ByteBuffer
import kotlin.experimental.and

class Request(private var mCmd: SerialCmd) : WriteEnable {
    private var tmp: ByteBuffer? = null
    private var mOffset = 0
    private var len = 0
    override fun write(buffer: ByteBuffer): Boolean {
        var r = true
        val remain = buffer.remaining()

        if (mOffset == 0 && remain > mCmd.mLength + 12) {
            wrapper(buffer)
            len = mCmd.mLength + 12
        } else {
            if (mCmd != null && mCmd.data != null) {
                len = mCmd.mLength + 12
            }
            if (tmp == null) {
                tmp = ByteBuffer.allocate(mCmd.mLength + 12)
                wrapper(tmp!!)
            }
            if (remain < tmp!!.position() - mOffset) {
                buffer.put(tmp!!.array(), mOffset, remain)
                mOffset += remain
                r = false
            } else {
                buffer.put(tmp!!.array(), mOffset, tmp!!.position() - mOffset)
                r = true
            }
        }
        return r
    }

    override fun getLen(): Int {
        return len
    }

    private fun wrapper(buffer: ByteBuffer) {
        val position = buffer.position()
        buffer.put(SerialCmd.START)
        buffer.put(SerialCmd.VER)
        Tools.append(buffer, mCmd.mLength, 4)
        buffer.put(mCmd.mDir)
        Tools.append(buffer, (mCmd.mNo and 0xff.toByte()).toInt(), 2)
        buffer.put(mCmd.data!!.toByteArray())
        try {
            val checkCode: Byte =
                Tools.computer(buffer.array(), position + 1, buffer.position() - position - 1)
            Tools.append(buffer, (checkCode and  0xff.toByte()).toInt(), 2)
            buffer.put(SerialCmd.END)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}