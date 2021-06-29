package com.johnmelodyme.bluetoothutilities.Constant;

public enum LogLevel
{
    DEBUG {
        public String toString()
        {
            return Constant.log_debug;
        }
    },

    INFO,

    VERBOSE
}
