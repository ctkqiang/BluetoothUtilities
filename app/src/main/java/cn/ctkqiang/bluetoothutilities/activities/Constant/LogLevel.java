package cn.ctkqiang.bluetoothutilities.Constant;

public enum LogLevel
{
    DEBUG {
        public String toString()
        {
            return Constant.log_debug;
        }
    },

    INFO {
        public String toString()
        {
            return Constant.log_info;
        }
    },

    VERBOSE {
        public String toString()
        {
            return Constant.log_verbose;
        }
    },

    ERROR {
        public String toString()
        {
            return Constant.log_error;
        }
    }
}
