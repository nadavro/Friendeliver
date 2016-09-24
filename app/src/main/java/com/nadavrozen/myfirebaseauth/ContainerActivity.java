package com.nadavrozen.myfirebaseauth;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContainerActivity extends ActionBarActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private static final int CONTENT_VIEW_ID = 10101010;
    private View header;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private User me;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,ProfileActivity.class));
//        }
        key = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference q = mDatabase.child("User").child(key);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ImageView img = (ImageView)header.findViewById(R.id.img_header);
                TextView name = (TextView)header.findViewById(R.id.name_header);
                name.setText(user.getFullName());
                if (user.getIsFacebook()==1){
                    String photoUrl = user.getUriImage();
                    new AsyncUploadImage(img).execute(photoUrl);
                }
                me = user;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        header = getLayoutInflater().inflate(R.layout.navaigaion_drawer_header, null);



        mDrawerList.addHeaderView(header);


        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Fragment fragment = new ProfileActivity();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void addDrawerItems() {
        String[] osArray = { "My Profile", "My Deliveries", "My Duties","My Requests" ,"Back to First Screen" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Fragment fragment;
                switch (position){
                    case 1: //edit profile
                        fragment = new UserProfile();
                        final Bundle bundle = new Bundle();
                        bundle.putParcelable("User",me);
                        bundle.putString("userKey",key);
                        fragment.setArguments(bundle);
                        break;
                    case 2: // my deliveries
                        fragment = new MyDeliveriesActivity();
                        break;
                    case 3: //my duties
                        fragment = new MyDutiesActivity();
                        break;
                    case 4://my requests
                        fragment = new MyRequestsActivity();
                        break;
                    default:
                        fragment = new ProfileActivity();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right).addToBackStack(null).commit();
                mDrawerLayout.closeDrawers();

            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
