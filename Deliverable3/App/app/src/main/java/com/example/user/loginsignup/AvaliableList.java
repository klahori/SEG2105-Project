package com.example.user.loginsignup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.loginsignup.R;
import com.example.user.loginsignup.Avalibility;

import java.util.List;

public class AvaliableList extends ArrayAdapter<Avalibility>  {

        private Activity context;
        List<Avalibility> avaliables;

        public AvaliableList(Activity context, List<Avalibility> avaliables) {
            super(context, R.layout.layout_avaliable_list, avaliables);
            this.context = context;
            this.avaliables = avaliables;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_avaliable_list, null, true);

            TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
            TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.textViewStartTime);
            TextView textViewEndTime = (TextView) listViewItem.findViewById(R.id.textViewEndTime);

            Avalibility avaliable = avaliables.get(position);
            textViewDate.setText(avaliable.getDate());
            textViewStartTime.setText(String.valueOf(avaliable.getStartTime()));
            textViewEndTime.setText(String.valueOf(avaliable.getEndTime()));

            return listViewItem;
        }
    }


