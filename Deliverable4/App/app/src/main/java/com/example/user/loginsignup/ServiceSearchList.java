package com.example.user.loginsignup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceSearchList extends ArrayAdapter<ServiceSearch> {

    private Activity context;
    List<ServiceSearch> services;


    public ServiceSearchList(Activity context, List<ServiceSearch> services) {
        super(context, R.layout.layout_service_search_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.layout_service_search_list,null, true);

        TextView serviceName = (TextView) listView.findViewById(R.id.serviceName);
        TextView cost = (TextView) listView.findViewById(R.id.servicePrice);
        TextView rating = (TextView) listView.findViewById(R.id.serviceRating);

        serviceName.setText(services.get(position).getServiceName());
        cost.setText(services.get(position).getCost());
        rating.setText(services.get(position).getRating());
        return listView;
    }
}
