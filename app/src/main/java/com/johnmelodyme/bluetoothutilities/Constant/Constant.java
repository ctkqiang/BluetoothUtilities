package com.johnmelodyme.bluetoothutilities.Constant;

public class Constant
{
    public static String TAG_NAME = "bluetooth";

    // Bluetooth Status
    public static String bluetooth_on = "BLUETOOTH ENABLED";
    public static String bluetooth_off = "BLUETOOTH DISABLED";
    public static String bluetooth_na = "BLUETOOTH NOT AVAILABLE";

    // Log Level
    public static String log_debug = "LOG LEVEL [DEBUG] => ";
    public static String log_info = "LOG LEVEL [INFO] => ";
    public static String log_verbose = "LOG LEVEL [VERBOSE] => ";
    public static String log_error = "LOG LEVEL [ERROR] => ";

    // Intents
    public static int REQUEST_ENABLE_BLUETOOTH = 0x0;
    public static int REQUEST_DISCOVERABLE = 0x0;
    public static final int REQUEST_DISCOVERABILITY_VALUE = 0x12c;
    public static final int REQUEST_DISCOVERABILITY = 0x1;

    // Warnings
    public static String bluetooth_required = "Bluetooth Required To Be Enabled";

    // Process
    public static String process_command = "logcat " + android.os.Process.myPid() + " *:D";

    // Url
    public static String source_code_url = "https://github.com/johnmelodyme/BluetoothUtilities";
}
