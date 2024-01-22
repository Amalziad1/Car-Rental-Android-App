package com.project.drivr.admin;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;


public class delete_customer extends Fragment {
    private DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_delete_customer, container, false);
        EditText fname=view.findViewById(R.id.customerfname);
        EditText lname=view.findViewById(R.id.customerlname);
        EditText email=view.findViewById(R.id.customerEmail);
        TextView emails=view.findViewById(R.id.emails);
        Button search=view.findViewById(R.id.search);
        Button delete=view.findViewById(R.id.deleteCustomer);
        dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(), "registration", null, 1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(fname.getText().toString())&& !TextUtils.isEmpty(lname.getText().toString())){
                    String firstName=fname.getText().toString().toLowerCase().replaceAll("\\s","");
                    String lastName=lname.getText().toString().toLowerCase().replaceAll("\\s","");
                    Cursor cursor= dataBaseHelper.getUserByNames(firstName,lastName);
                    if(cursor!=null && cursor.moveToLast()){
                        if(cursor.getCount()>1){
                            while(cursor.moveToNext()){//if more than one, just display them all
                                @SuppressLint("Range") String Email=cursor.getString(cursor.getColumnIndex("EMAIL"));
                                String info=emails.getText().toString();
                                info.concat("\n"+Email);
                                emails.setText(info);
                            }
                        }else{//if just one, display it in both emails and email edittext
                            @SuppressLint("Range") String Email=cursor.getString(cursor.getColumnIndex("EMAIL"));
                            emails.setText(emails.getText().toString().concat("\n"+Email));
                            email.setText(Email);
                        }
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Not found...", Toast.LENGTH_SHORT).show();
                    }
                    if(cursor !=null){
                        cursor.close();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email.getText().toString())){
                    boolean deletion= dataBaseHelper.deleteUserByEmail(email.getText().toString().toLowerCase());
                    if(deletion){
                        Toast.makeText(getActivity().getApplicationContext(), "Deletion succeeded!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Deletion Failed...", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "No specified Email to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}