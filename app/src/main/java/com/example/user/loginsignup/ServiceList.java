package com.example.user.loginsignup;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceList extends ArrayAdapter<Service> {
    private Activity context;
    List<Service> services;

    public ServiceList(Activity context, List<Service> services) {
        super(context, R.layout.layout_service_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_service_list, null, true);

        TextView textViewService = (TextView) listViewItem.findViewById(R.id.textViewService);
        TextView textViewCost = (TextView) listViewItem.findViewById(R.id.textViewCost);

        Service service = services.get(position);
        textViewService.setText(service.getServiceName());
        textViewCost.setText(String.valueOf(service.getCost()));
        return listViewItem;
    }
}


