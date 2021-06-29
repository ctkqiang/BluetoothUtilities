package com.johnmelodyme.bluetoothutilities.Constant;

public enum BluetoothStatus
{
    BLUETOOTH_ENABLED {
        public String toString()
        {
            return Constant.bluetooth_on;
        }
    },

    BLUETOOTH_DISABLED {
        public String toString()
        {
            return Constant.bluetooth_off;
        }

    },

    BLUETOOTH_NOT_FOUND {
        public String toString()
        {
            return Constant.bluetooth_na;
        }
    },
}
