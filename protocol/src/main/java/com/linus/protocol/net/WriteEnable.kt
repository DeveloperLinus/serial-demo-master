package com.linus.protocol.net

import java.nio.ByteBuffer

// 写数据接口
interface WriteEnable {
    fun write(buffer:ByteBuffer): Boolean
    fun getLen(): Int
}