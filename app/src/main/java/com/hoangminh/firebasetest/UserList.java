package com.hoangminh.firebasetest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hoangminh.firebasetest.Model.User;

import java.util.List;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    List<User> users;

    public UserList(Activity context, List<User> users){
        super(context, R.layout.layout_user_list, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewCity = (TextView) listViewItem.findViewById(R.id.textViewCity);

        User user = users.get(postion);
        textViewName.setText(user.getUserName());
        textViewCity.setText(user.getUserCity());
        return listViewItem;
    }
}
