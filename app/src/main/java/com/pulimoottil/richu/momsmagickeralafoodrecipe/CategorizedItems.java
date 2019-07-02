package com.pulimoottil.richu.momsmagickeralafoodrecipe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class CategorizedItems extends AppCompatActivity{

    private Boolean mProcessLike = false;
    private RecyclerView mResultList;
    int flag = 1;
    String key;
    TextView heading_label;
    ProgressDialog progress;
    String uid;
    FirebaseRecyclerAdapter<Items, SearchItemsViewHolder> firebaseRecyclerAdapter;
    FirebaseUser currentUser;
    DatabaseReference mUserDatabase;
    NavigationView navigationView;
    DatabaseReference mDatabaseLikes;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String mUsername,mPhoto;
    Uri mPhotoUri;
    View hView;
    CircleImageView profile_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized_items);
        //nav item
        currentUser = mAuth.getCurrentUser();
        navigationView = findViewById(R.id.nav_view);
        mUsername = currentUser.getDisplayName();
        mPhotoUri = currentUser.getPhotoUrl();
        mPhoto = mPhotoUri.toString();
        hView =  navigationView.inflateHeaderView(R.layout.navigation_header);
        profile_image = hView.findViewById(R.id.profile_image);


        TextView display_name = (TextView)hView.findViewById(R.id.display_name);
        display_name.setText(mUsername);
        //fab
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategorizedItems.this, GroceryListActivity.class);
                startActivity(intent);
            }
        });
        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLikes.keepSynced(true);

        uid = currentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("food");
        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        Query firebaseSearchQuery = mUserDatabase.orderByChild("category");
        heading_label = findViewById(R.id.heading_label);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, SearchItemsViewHolder>(

                Items.class,
                R.layout.items_list_layout,
                SearchItemsViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(SearchItemsViewHolder viewHolder, Items model, int position) {
                //viewHolder.setDetails(getContext(), model.getTitle(), model.getYoutubeUrl(), model.getImageUrl());

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
        progress =  new ProgressDialog(this);
        progress.setTitle("Searching");
        progress.setMessage("Wait while searching...");
        progress.setCancelable(false);
        progress.show();
        Intent intent = getIntent();
        String searchText = intent.getStringExtra("selectedCategory");
        heading_label.setText(searchText);
        progress.dismiss();
        firebaseUserSearch(searchText);
        mDrawerLayout = findViewById(R.id.drawerLayout);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(CategorizedItems.this, UserLogin.class));
                                finish();
                                break;
                            case R.id.nav_favourites:
                                startActivity(new Intent(CategorizedItems.this, FavouritesActivity.class));
                                finish();
                                break;
                            case R.id.nav_home:
                                startActivity(new Intent(CategorizedItems.this, HomeActivity.class));
                                finish();
                                break;
                            case R.id.nav_health_tips:
                                startActivity(new Intent(CategorizedItems.this, HealthTips.class));
                                finish();
                                break;
                            default:
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }//oncreate

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_report:
                startActivity(new Intent(this, ReportProblem.class));
                return true;
            case R.id.credits:
                startActivity(new Intent(this, CreditsActivity.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void firebaseUserSearch(String searchText) {

        //Toast.makeText(this, "Started Search for "+searchText, Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("category").equalTo(searchText);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, SearchItemsViewHolder>(

                Items.class,
                R.layout.items_list_layout,
                SearchItemsViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            public void onBindViewHolder(SearchItemsViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
            }

            @Override
            protected void populateViewHolder(final SearchItemsViewHolder viewHolder, Items model, final int position) {
                viewHolder.setDetails(CategorizedItems.this, model.getTitle(),model.getImageUrl(), model.getVideoBy(),model.getLikescount());
                key = getRef(position).getKey();;
                viewHolder.setLikeButton(key);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flag = 1;
                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    final int key = Integer.parseInt(getRef(position).getKey());
                                    if(flag == key){
                                        String YOUTUBE_VIDEO_CODE = snapshot.child("youtubeurl").getValue().toString();
                                        String title = snapshot.child("title").getValue().toString();
                                        String ingredients = snapshot.child("ingredients").getValue().toString().replace("_b","\n");
                                        Intent intent = new Intent(CategorizedItems.this, YoutubeFoodRecipeSearch.class);
                                        intent.putExtra("YOUTUBE_VIDEO_CODE",YOUTUBE_VIDEO_CODE);
                                        intent.putExtra("title",title);
                                        intent.putExtra("ingredients",ingredients);
                                        //((Activity)mCtx).finish();
                                        startActivity(intent);
                                        break;
                                    }
                                    else {
                                        flag++;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.image_like.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        mProcessLike = true;
                            mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    key = getRef(position).getKey();
                                    if(mProcessLike){
                                        if(dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid())){
                                            mDatabaseLikes.child(key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;
                                        }else{
                                            key = getRef(position).getKey();
                                            mDatabaseLikes.child(key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                            mProcessLike = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    }
                });

            }
        };




        mResultList.setAdapter(firebaseRecyclerAdapter);



    }



    // View Holder Class

    public static class SearchItemsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView image_like;
        DatabaseReference mDatabaseLikes;
        FirebaseAuth mAuth;


        public SearchItemsViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            image_like = itemView.findViewById(R.id.image_like);
            image_like.setVisibility(View.VISIBLE);
            mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

        }

        public void setLikeButton(final String key){
            mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("richuuid",""+mAuth.getCurrentUser().getUid());
                    Log.e("richufalse",""+dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid()));
                    if(dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid())){
                        Log.e("richutrue","richutrue");
                        image_like.setImageResource(R.drawable.loved);
                    }else{
                        Log.e("richufalse","richufalse");

                        image_like.setImageResource(R.drawable.love);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        public void setDetails(Context ctx, String title, String image, String videoby,String likescount) {

            TextView user_name = mView.findViewById(R.id.name_text);
            TextView user_status = mView.findViewById(R.id.status_text);
            ImageView user_image = mView.findViewById(R.id.profile_image);





            user_image.setVisibility(View.VISIBLE);
            user_name.setVisibility(View.VISIBLE);
            user_status.setVisibility(View.VISIBLE);
            user_name.setText(title);
            user_status.setText(videoby);
            Picasso.get().load(image).into(user_image);


        }


    }


}
