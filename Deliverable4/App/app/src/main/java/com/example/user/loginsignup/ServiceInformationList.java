package com.example.user.loginsignup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceInformationList extends ArrayAdapter<ServiceInformation> {

    private Activity context;
    List<ServiceInformation> service;

    public ServiceInformationList (Activity context, List<ServiceInformation> service) {
        super(context, R.layout.layout_service_information_list, service);
        this.context = context;
        this.service = service;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.layout_service_information_list, null, true);

        TextView serviceName = (TextView) listView.findViewById(R.id.serviceNameInfo);
        TextView cost = (TextView) listView.findViewById(R.id.costInfo);
        TextView rating = (TextView) listView.findViewById(R.id.ratingInfo);
        TextView date = (TextView) listView.findViewById(R.id.dateInfo);
        TextView startDate = (TextView) listView.findViewById(R.id.startDateInfo);
        TextView endDate = (TextView) listView.findViewById(R.id.endDateInfo);

        serviceName.setText(service.get(position).getServiceName());
        cost.setText("Cost: " + String.valueOf(service.get(position).getCost()));
        rating.setText("Rating: "+ service.get(position).getRating());
        date.setText(service.get(position).getDate());
        startDate.setText(service.get(position).getStartTime());
        endDate.setText(service.get(position).getEndTime());

        return listView;
    }
}
