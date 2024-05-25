package cn.ctkqiang.bluetoothutilities.model;

import android.os.ParcelUuid;

public class DiscoveredDevices
{
    String name;
    String address;
    int state;
    int type;
    ParcelUuid[] uuid;

    public DiscoveredDevices(String name,
                             String address,
                             int state,
                             int type,
                             ParcelUuid[] uuid)
    {
        this.name = name;
        this.address = address;
        this.state = state;
        this.type = type;
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public ParcelUuid[] getUuid()
    {
        return uuid;
    }

    public void setUuid(ParcelUuid[] uuid)
    {
        this.uuid = uuid;
    }
}
