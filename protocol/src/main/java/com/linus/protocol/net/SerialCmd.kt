package com.linus.protocol.net

import com.linus.common.utils.Tools

class SerialCmd {
    companion object {
        val VER = String.format("%02x", 0).toByteArray()
        private const val REQUEST: Byte = 0x30
        private const val ANSWER: Byte = 0x31
        const val START: Byte = 0x2
        const val END: Byte = 0x3

        fun create(d: ByteArray?): SerialCmd? {
            val serialCmd = SerialCmd()
            serialCmd.mVersion = Tools.getInteger(d, 1, 2)
            serialCmd.mLength = Tools.getInteger(d, 3, 4)
            serialCmd.mDir = Tools.getInteger(d, 7, 1).toByte()
            serialCmd.mNo = Tools.getInteger(d, 8, 2).toByte()
            serialCmd.data = d?.let { String(it, 10, serialCmd.mLength - 3) }
            serialCmd.mCheck = Tools.getInteger(d, 10 + serialCmd.mLength - 3, 2)
            return serialCmd
        }
    }

    private var mVersion = 0
    var mLength = 0
    var mDir: Byte = 0
    var mNo: Byte = 0
    var data: String? = null
    private var mCheck = 0

    class Builder {
        private var mNo: Byte = 0
        private var mData: String? = null
        private var isReq = false
        fun setData(data: String?) {
            mData = data
        }

        fun setNo(no: Byte) {
            mNo = no
        }

        fun setDirection(isReq: Boolean) {
            this.isReq = isReq
        }

        fun create(): WriteEnable {
            var cmd = SerialCmd().apply {
                data = mData
                this.mNo = mNo
                mDir = if (isReq) REQUEST else ANSWER
                mLength = mData?.toByteArray()?.size?.plus(3) ?: 0
            }
            return Request(cmd)
        }
    }
}