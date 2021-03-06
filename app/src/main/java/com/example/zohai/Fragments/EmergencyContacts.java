package com.example.zohai.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.zohai.healthapp.R;

public class EmergencyContacts extends Monitor {
    private EditText phone1;
    private EditText phone2;
    private String shared_mobile1;
    private String shared_mobile2;

    private Button delete;

    SharedPreferences sharedPreferences;


    public static EmergencyContacts newInstance()
    {
        EmergencyContacts fragment = new EmergencyContacts();
        return fragment;
    }


    public EmergencyContacts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("myContact",Context.MODE_PRIVATE);
        shared_mobile1 = sharedPreferences.getString("phone1",null);
        shared_mobile2 = sharedPreferences.getString("phone2",null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);
        phone1 = (EditText) view.findViewById(R.id.tvNumber);
        phone2 = (EditText) view.findViewById(R.id.tvNumber1);
        delete = (Button) view.findViewById(R.id.delnumbers);
       final Button updateContact = (Button) view.findViewById(R.id.updatenumbers);

//        phone1.setText(sharedPreferences.getString("phone1",""));
//        phone2.setText(sharedPreferences.getString("phone2",""));

        phone1.setText(shared_mobile1);
        phone2.setText(shared_mobile2);

        phone1.setEnabled(false);
        phone2.setEnabled(false);

        updateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getActivity().getSharedPreferences("myContact",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("phone1",phone1.getText().toString());
                editor.putString("phone2",phone2.getText().toString());
                editor.commit();

                Toast.makeText(getActivity(),"Please press monitor button below...",Toast.LENGTH_SHORT).show();
                phone1.setEnabled(false);
                phone2.setEnabled(false);
                updateContact.setVisibility(View.INVISIBLE);

            }
        });

        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("myContact",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("phone1");
                editor.remove("phone2");
                editor.clear();
                editor.commit();
                phone1.setText("");
                phone2.setText("");

            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               phone1.setEnabled(true);
                phone2.setEnabled(true);
                updateContact.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}
