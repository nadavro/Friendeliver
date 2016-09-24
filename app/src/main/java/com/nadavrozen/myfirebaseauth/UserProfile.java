package com.nadavrozen.myfirebaseauth;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The profile of the user including image,reviews and other personal detail
 */
public class UserProfile extends Fragment {
    private User currentUser;
    private String userKey;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ArrayList<Review> userReviews;
    private ListView listView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,ProfileActivity.class));
//        }

        user = firebaseAuth.getCurrentUser();
        String key = firebaseAuth.getCurrentUser().getUid();



        //getting the currentUser from the last fragment
        Bundle extras = this.getArguments();
        if (extras != null) {
            currentUser = extras.getParcelable("User");
            userKey = extras.getString("userKey");

        }

        listView = (ListView)view.findViewById(R.id.reviews);
        ImageView imgView = (ImageView)view.findViewById(R.id.img);
        TextView nameView = (TextView)view.findViewById(R.id.name);
        TextView emailView = (TextView)view.findViewById(R.id.email);
        ImageView linkView = (ImageView)view.findViewById(R.id.facebook);
        TextView phoneView = (TextView)view.findViewById(R.id.phonenum);

        String name = currentUser.getFullName();
        String email = currentUser.getEmail();
        if(currentUser.getIsFacebook()==1){
            //System.out.println("is facecbook");
            String photoUrl = currentUser.getUriImage();
            linkView.setVisibility(View.VISIBLE);
//            Uri uri = Uri.parse(photoUrl);
//            imgView.setImageURI(null);
//            imgView.setImageURI(uri);
            new AsyncUploadImage(imgView).execute(photoUrl);
            final String facebookLink = currentUser.getFacebookLink();

            //linkView.setText("Facebook link");
            linkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(facebookLink); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }


        String phone = currentUser.getPhone();


        nameView.setText(name);
        emailView.setText(email);

        userReviews = new ArrayList<Review>();

        //retrieve reviews about the current user
        Query query = mDatabase.child("Review").orderByChild("uid").equalTo(userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Review> temp = new ArrayList<Review>();
                for (DataSnapshot usi : dataSnapshot.getChildren()){
                    Review review = usi.getValue(Review.class);
                    temp.add(review);
                }
                setReviewList(temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return  view;
    }

    private void setReviewList(ArrayList<Review> temp) {
        this.userReviews = temp;

        ReviewsAdapter adapter = new ReviewsAdapter(getActivity().getApplicationContext(),
                R.layout.reviews_row, userReviews);
        listView.setAdapter(adapter);



    }
}
