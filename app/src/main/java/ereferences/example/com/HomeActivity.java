package ereferences.example.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
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
    private FloatingActionButton uploadBookFloatingActionButton;
    private ArrayList<String> images;
    ListView book_list;
    ArrayAdapter<String> stringArrayAdapter;

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
        final Intent intent = getIntent();
        loginType = intent.getStringExtra("KEY_LOGIN_TYPE");

        initView();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (loginType.equals(AppConstant.FIREBASE_TABLE_STUDNET)) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.home);
            uploadBookFloatingActionButton.setVisibility(View.INVISIBLE);
        }

        uploadBookFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAddBook = new Intent(HomeActivity.this, AddBookActivity.class);
                startActivity(gotoAddBook);
            }
        });

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

        book_list = findViewById(R.id.book_list);
        uploadBookFloatingActionButton = findViewById(R.id.fab);
        images = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_expandable_list_item_1, images);
        book_list.setAdapter(stringArrayAdapter);
        fetchImages();

        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parent.getItemAtPosition(position).toString()));
                startActivity(browserIntent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void fetchImages() {


        DataRef.child(AppConstant.FIREBASE_TABLE_BOOK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot imglist : dataSnapshot.getChildren()) {
                    for (DataSnapshot img : imglist.getChildren()) {
                        BookImage bookImage = new BookImage();

                        if (img.getKey().equals("thumbUrl")) {
                            Log.e("THUMM", img.getValue() + "");
                            bookImage.setUrl(img.getValue().toString());
                            images.add(img.getValue().toString());
                        }
                        if (img.getKey().equals("bookTitle")) {
                            bookImage.setName(img.getValue().toString());
                        }

                        stringArrayAdapter.notifyDataSetChanged();
                    }
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
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_add_category) {
            Intent gotoAddCategory = new Intent(HomeActivity.this, AddCategoryActivity.class);
            startActivity(gotoAddCategory);
        } else if (id == R.id.menu_view_category) {
            Intent gotoViewCategory = new Intent(HomeActivity.this, ViewCategoryActivity.class);
            startActivity(gotoViewCategory);
        } else if (id == R.id.menu_signout) {
            auth.signOut();
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
