package com.example.zohai.healthapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zohai.Fragments.BMI;
import com.example.zohai.Fragments.BMR;
import com.example.zohai.Fragments.EmergencyContacts;
import com.example.zohai.Fragments.Monitor;
import com.example.zohai.Fragments.Profile;
import com.example.zohai.Fragments.Records;
import com.example.zohai.Fragments.Reminder;
import com.example.zohai.Fragments.Settings;
import com.example.zohai.Fragments.WaterNeeds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean exit = false;

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String firebaseUser;

    private View navHeader;
    private TextView txtName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        auth = FirebaseAuth.getInstance();

        //bottom navigation view listener
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_monitor:
                        selectedFragment = Monitor.newInstance();
                        break;
                    case R.id.navigation_records:
                        selectedFragment = Records.newInstance();
                        break;
                    case R.id.navigation_profile:
                        selectedFragment = Profile.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, selectedFragment);
                transaction.commit();
                return true;
            }

        };
        BottomNavigationView btmnavigation = (BottomNavigationView) findViewById(R.id.navigation);
        btmnavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, Monitor.newInstance());
        transaction.commit();

        //navigation drawer initialization
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.navname);
        txtEmail = (TextView) navHeader.findViewById(R.id.navemail);

        loadnavHeader();
    }

    //load nav header with username and email
    private void loadnavHeader() {
        mFirebaseDatabase.child(firebaseUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "No user found", Toast.LENGTH_SHORT).show();
                    return;
                }
                    else
                    {
                        txtName.setText(user.getName());
                        txtEmail.setText(user.getEmail());
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (exit)
        {
            this.finishAffinity();

        }
        else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard2, menu);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_signout) {
                SharedPreferences sp1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences sp2 = getSharedPreferences("myContact",Context.MODE_PRIVATE);
                SharedPreferences sp3 = getSharedPreferences("HealthValues",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp1.edit();
                SharedPreferences.Editor editor1 = sp2.edit();
                SharedPreferences.Editor editor2 = sp3.edit();
                editor.clear();
                editor1.clear();
                editor2.clear();
                editor1.apply();
                editor.apply();
                editor2.apply();
                auth.signOut();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bmi) {
            fragmentClass= BMI.class;

        } else if (id == R.id.nav_bmr) {
            fragmentClass= BMR.class;

        } else if (id == R.id.nav_weight) {
            fragmentClass= Reminder.class;

        } else if (id == R.id.nav_settings) {
            fragmentClass = Settings.class;

        }else if (id == R.id.nav_emergency) {
                fragmentClass= EmergencyContacts.class;
        }
        else if (id == R.id.nav_water) {
            fragmentClass= WaterNeeds.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
