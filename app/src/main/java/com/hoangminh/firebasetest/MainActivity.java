package com.hoangminh.firebasetest;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoangminh.firebasetest.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    EditText editTextName;
    Spinner spinnerCity;
    Button buttonAddUser;
    ListView listViewUsers;

    //Tao List user
    List<User> users;

    DatabaseReference databaseUsers;

    private boolean updateUser(String id, String name, String city) {
        //Lay node users
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //updating user
        User user = new User(id, name, city);
        dR.setValue(user);
        Toast.makeText(getApplicationContext(), "Đã cập nhật!", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //lay node users
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //Xoa user
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Đã xóa user", Toast.LENGTH_LONG).show();

        return true;
    }

    private void showUpdateDeleteDialog(final String userId, String userName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerCity = (Spinner) dialogView.findViewById(R.id.spinnerCity);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(userName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String city = spinnerCity.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateUser(userId, name, city);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(userId);
                b.dismiss();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lay users node
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        buttonAddUser = (Button) findViewById(R.id.buttonAddUsers);

        //list store users
        users = new ArrayList<>();


        //Button Adduser
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addUser()
                //the method is defined below
                //this method is actually performing the write operation
                addUser();
            }
        });

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                showUpdateDeleteDialog(user.getUserId(), user.getUserName());
                return true;
            }
        });
    }

    /*
     * Luu user vao firebase
     * */
    private void addUser() {
        //lay du lieu
        String name = editTextName.getText().toString().trim();
        String city = spinnerCity.getSelectedItem().toString();

        //kiem tra xem da co du lieu hay chua
        if (!TextUtils.isEmpty(name)) {

            //lay id
            String id = databaseUsers.push().getKey();

            //tao chuoi json chua du lieu
            User user = new User(id, name, city);

            //luu user
            databaseUsers.child(id).setValue(user);
            //cho editText ve blank
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Đã thêm!", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                //chay vong lap
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    //Lấy dữ liệu
                    User user = postSnapshot.getValue(User.class);
                    users.add(user);
                }

                UserList userAdapter = new UserList(MainActivity.this, users);

                listViewUsers.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
