package ereferences.example.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookThumbAdapter.ItemListener {


    //    Component Initlization
    private TextView userNameTv;
    private TextView userEmailTv;
    private FloatingActionButton uploadBookFloatingActionButton;


    private ArrayList<BookDataModel> bookDataModelArrayList;
    private ArrayList<BookDataModel> cat1;
    private ArrayList<BookDataModel> cat2;
    private BookThumbAdapter bookThumbAdapter;

    RecyclerView bookListRecyclerView;

    //    Firebase Init
    private DatabaseReference DataRef;
    private FirebaseAuth auth;


    //    variable
    private String loginType;
    private BookThumbAdapter cat1adapter;
    private BookThumbAdapter cat2adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        auth = FirebaseAuth.getInstance();
        DataRef = FirebaseDatabase.getInstance().getReference();
        final Intent intent = getIntent();
        loginType = intent.getStringExtra("KEY_LOGIN_TYPE");

        initView();
        FirebaseMessaging.getInstance().subscribeToTopic("all");

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
        cat1 = new ArrayList<>();
        cat2 = new ArrayList<>();


        bookThumbAdapter = new BookThumbAdapter(HomeActivity.this, bookDataModelArrayList, this);
        cat1adapter = new BookThumbAdapter(HomeActivity.this, cat1, this);
        cat2adapter = new BookThumbAdapter(HomeActivity.this, cat2, this);
//        bookListRecyclerView.setAdapter(stringArrayAdapter);
        fetchImages();

        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.first_recycler_view);
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(bookThumbAdapter);
        Log.e("LIST", bookDataModelArrayList.toString());


        MultiSnapRecyclerView secondRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.second_recycler_view);
        LinearLayoutManager secondManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(cat1adapter);


        MultiSnapRecyclerView thirdRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.third_recycler_view);
        LinearLayoutManager thirdManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        thirdRecyclerView.setLayoutManager(thirdManager);
        thirdRecyclerView.setAdapter(cat2adapter);

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

//                                    Toast.makeText(HomeActivity.this, "" + bookList.getKey(), Toast.LENGTH_SHORT).show();
                                    if (bookList.child("bookUrl").getValue() != null && bookList.child("bookTitle") != null) {
                                        bookDataModel.setBookKey(bookList.getKey());
                                        bookDataModel.setBookName(bookList.child("bookName").getValue().toString());
                                        bookDataModel.setBookUrl(bookList.child("bookUrl").getValue().toString());
                                        bookDataModel.setCategory(categoryList.child("categoryName").getValue().toString());
                                        bookDataModel.setThumbUrl(bookList.child("thumbUrl").getValue().toString());
                                        Log.e("LIST", bookDataModel.getBookName());
                                        bookDataModelArrayList.add(bookDataModel);
                                        bookThumbAdapter.notifyDataSetChanged();

                                        if (categoryList.child("categoryName").getValue().equals("Artificial Intelligence")) {
                                            cat1.add(bookDataModel);
                                            cat1adapter.notifyDataSetChanged();
                                        }
                                        if (categoryList.child("categoryName").getValue().equals("Android")) {
                                            cat2.add(bookDataModel);
                                            cat2adapter.notifyDataSetChanged();
                                        }
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
            final Intent gotoAddCategory = new Intent(HomeActivity.this, AddCategoryActivity.class);
            startActivity(gotoAddCategory);
        } else if (id == R.id.menu_view_category) {
            final Intent gotoViewCategory = new Intent(HomeActivity.this, ViewCategoryActivity.class);
            startActivity(gotoViewCategory);
        } else if (id == R.id.menu_signout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            builder.setMessage("Do you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            auth.signOut();
                            finish();
                        }
                    });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();

            alertDialog.setTitle("Account Action");
            alertDialog.show();

        } else if (id == R.id.menu_download_book) {
            final Intent gotoOfflineBook = new Intent(HomeActivity.this, BookOfflineActivity.class);
            startActivity(gotoOfflineBook);
        } else if (id == R.id.menu_new_book_request) {
            final Intent gotoRequestBook = new Intent(HomeActivity.this, RequestBookActivity.class);
            startActivity(gotoRequestBook);
        } else if (id == R.id.menu_request_book_list) {
            final Intent gotoRequestBookList = new Intent(HomeActivity.this, RequestBookListActivity.class);
            startActivity(gotoRequestBookList);
        } else if (id == R.id.menu_aboutapp) {
            final Intent gotoAboutApp = new Intent(HomeActivity.this, AboutAppActivity.class);
            startActivity(gotoAboutApp);
        } else if (id == R.id.menu_developer) {
            final Intent gotoMenuDeveloper = new Intent(HomeActivity.this, DeveloperActivity.class);
            startActivity(gotoMenuDeveloper);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onItemClick(BookDataModel item) {


        final Intent gotoBookDetails = new Intent(HomeActivity.this, BookDetailsActivity.class);

        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_PUSH_KEY, item.getBookKey());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_NAME, item.getBookName());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_CATEGORY, item.getCategory());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_URL, item.getBookUrl());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_THUMB, item.getThumbUrl());

        startActivity(gotoBookDetails);

    }
}
