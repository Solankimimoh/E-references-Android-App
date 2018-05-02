package ereferences.example.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookThumbAdapter.ItemListener {


    //    Component Initlization
    private TextView userNameTv;
    private TextView userEmailTv;
    private FloatingActionButton uploadBookFloatingActionButton;


    private ArrayList<BookDataModel> bookDataModelArrayList;
    private BookThumbAdapter bookThumbAdapter;

    RecyclerView bookListRecyclerView;

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

//        bookListRecyclerView = findViewById(R.id.);
        uploadBookFloatingActionButton = findViewById(R.id.fab);

        bookDataModelArrayList = new ArrayList<>();
        bookThumbAdapter = new BookThumbAdapter(HomeActivity.this, bookDataModelArrayList, this);
//        bookListRecyclerView.setAdapter(stringArrayAdapter);
        fetchImages();

        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.first_recycler_view);
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(bookThumbAdapter);
        Log.e("LIST", bookDataModelArrayList.toString());


        MultiSnapRecyclerView secondRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.second_recycler_view);
        LinearLayoutManager secondManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(bookThumbAdapter);


        MultiSnapRecyclerView thirdRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.third_recycler_view);
        LinearLayoutManager thirdManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        thirdRecyclerView.setLayoutManager(thirdManager);
        thirdRecyclerView.setAdapter(bookThumbAdapter);

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

                if (!bookDataModelArrayList.isEmpty()) {
                    bookDataModelArrayList.clear();
                }


                for (final DataSnapshot bookList : dataSnapshot.getChildren()) {

                    DataRef.child(AppConstant.FIREBASE_TABLE_CATEGORY).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (!bookDataModelArrayList.isEmpty()) {
//                                bookDataModelArrayList.clear();
//                            }

                            for (DataSnapshot categoryList : dataSnapshot.getChildren()) {
                                BookDataModel bookDataModel = new BookDataModel();
//
                                // Log.e("BOOK", categoryList.child("categoryName").getValue().toString());
                                Log.e("BOOK", bookList.child("category").getValue() + "");
                                Log.e("BOOK", categoryList.getKey() + "");

                                if (categoryList.getKey().equals(bookList.child("category").getValue())) {

                                    if (bookList.child("bookUrl").getValue() != null && bookList.child("bookTitle") != null) {
                                        bookDataModel.setBookName(bookList.child("bookName").getValue().toString());
                                        bookDataModel.setBookUrl(bookList.child("bookUrl").getValue().toString());
                                        bookDataModel.setCategory(categoryList.child("categoryName").getValue().toString());
                                        bookDataModel.setThumbUrl(bookList.child("thumbUrl").getValue().toString());
                                        Log.e("LIST", bookDataModel.getBookName());
                                        bookDataModelArrayList.add(bookDataModel);
                                        bookThumbAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(HomeActivity.this, "NOT RUN", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


                bookThumbAdapter.notifyDataSetChanged();
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
        // Handle navigation view bookDataModel clicks here.
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

    @Override
    public void onItemClick(BookDataModel item) {


        final Intent gotoBookDetails = new Intent(HomeActivity.this, BookDetailsActivity.class);

        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_NAME, item.getBookName());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_CATEGORY, item.getCategory());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_URL, item.getBookUrl());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_THUMB, item.getThumbUrl());

        startActivity(gotoBookDetails);

    }
}
