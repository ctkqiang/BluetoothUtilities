package cn.ctkqiang.bluetoothutilities.user_interface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.ctkqiang.bluetoothutilities.R;
import cn.ctkqiang.bluetoothutilities.model.DiscoveredDevices;

import java.util.ArrayList;
import java.util.Arrays;


public class BluetoothCustomAdapter extends ArrayAdapter<DiscoveredDevices> implements
                                                                            Filterable
{
    public Context context;
    public ArrayList<DiscoveredDevices> discoveredDevices;
    public ArrayList<DiscoveredDevices> discovered_devices;

    private static class ViewHolder
    {
        TextView name;
        TextView address;
        TextView type;
        TextView state;
        TextView uuid;
    }

    public BluetoothCustomAdapter(ArrayList<DiscoveredDevices> discovereddevices,
                                  @NonNull Context context)
    {
        super(context, R.layout.custom_devices_discovered, discovereddevices);
        this.discoveredDevices = discovereddevices;
        this.context = context;
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults = new FilterResults();
                discovered_devices = new ArrayList<DiscoveredDevices>();

                if (constraint != null)
                {
                    if (discoveredDevices != null && discoveredDevices.size() > 0)
                    {
                        for (DiscoveredDevices d : discoveredDevices)
                        {
                            if (d.getName().contains(constraint.toString()))
                            {
                                discovered_devices.add(d);
                            }
                        }
                    }

                    filterResults.values = discovered_devices;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                discoveredDevices = (ArrayList<DiscoveredDevices>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return discoveredDevices.size();
    }

    @Override
    public DiscoveredDevices getItem(int position) {
        return discoveredDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder = new ViewHolder();
        View result;
        DiscoveredDevices discovered = getItem(position);

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_devices_discovered, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.state = (TextView) convertView.findViewById(R.id.bond);
            viewHolder.uuid = (TextView) convertView.findViewById(R.id.uuid);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText("Bluetooth Name: " + discovered.getName());
        viewHolder.address.setText("Bluetooth Address: " + discovered.getAddress());
        viewHolder.type.setText("Bluetooth Type: " + discovered.getType());
        viewHolder.state.setText("Bluetooth Bond State: " + discovered.getState());
        viewHolder.uuid.setText("Bluetooth UUID: \n" + Arrays.toString(discovered.getUuid()));

        return convertView;
    }

    private static class SearchFilter extends Filter
    {

        /**
         * <p>Invoked in a worker thread to filter the data according to the
         * constraint. Subclasses must implement this method to perform the
         * filtering operation. Results computed by the filtering operation
         * must be returned as a {@link FilterResults} that
         * will then be published in the UI thread through
         * {@link #publishResults(CharSequence,
         * FilterResults)}.</p>
         *
         * <p><strong>Contract:</strong> When the constraint is null, the original
         * data must be restored.</p>
         *
         * @param constraint the constraint used to filter the data
         * @return the results of the filtering operation
         * @see #filter(CharSequence, FilterListener)
         * @see #publishResults(CharSequence, FilterResults)
         * @see FilterResults
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            return null;
        }

        /**
         * <p>Invoked in the UI thread to publish the filtering results in the
         * user interface. Subclasses must implement this method to display the
         * results computed in {@link #performFiltering}.</p>
         *
         * @param constraint the constraint used to filter the data
         * @param results    the results of the filtering operation
         * @see #filter(CharSequence, FilterListener)
         * @see #performFiltering(CharSequence)
         * @see FilterResults
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {

        }
    }
}