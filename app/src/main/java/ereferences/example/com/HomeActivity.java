package ereferences.example.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //    Component Initlization
    private TextView userNameTv;
    private TextView userEmailTv;

    //    Firebase Init
    private DatabaseReference DataRef;
    private FirebaseAuth auth;


    //    variable
    private String loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        DataRef = FirebaseDatabase.getInstance().getReference();

        initView();
        final Intent intent = getIntent();
        loginType = intent.getStringExtra("KEY_LOGIN_TYPE");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (loginType.equals(AppConstant.FIREBASE_TABLE_STUDNET)) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.home);
        }
        navigationView.setNavigationItemSelectedListener(this);

        //        get user name and email=
        final View headerView = navigationView.getHeaderView(0);
        userNameTv = headerView.findViewById(R.id.nav_header_home_username);
        userEmailTv = headerView.findViewById(R.id.nav_header_home_email);

        DataRef.child(loginType)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Toast.makeText(HomeActivity.this, "Welcome " + dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString(), Toast.LENGTH_SHORT).show();
                        final String userName = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString();
                        final String userEmail = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EMAIL).getValue().toString();
                        userNameTv.setText(userName);
                        userEmailTv.setText(userEmail);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.e("TAG", "Failed to read value.", error.toException());
                    }
                });

    }

    private void initView() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
