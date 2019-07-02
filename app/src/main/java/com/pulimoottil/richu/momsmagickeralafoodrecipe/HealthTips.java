package com.pulimoottil.richu.momsmagickeralafoodrecipe;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthTips extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String mEmail,mUsername,mId,mPhoto;
    Uri mPhotoUri;
    NavigationView navigationView;
    View hView;
    FirebaseUser currentUser;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);
        //fab
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthTips.this, GroceryListActivity.class);
                startActivity(intent);
            }
        });
        currentUser = mAuth.getCurrentUser();
        //drawer
        currentUser = mAuth.getCurrentUser();
        mUsername = currentUser.getDisplayName();
        mEmail = currentUser.getEmail();
        mId = currentUser.getUid();
        mPhotoUri = currentUser.getPhotoUrl();
        mPhoto = mPhotoUri.toString();
        navigationView = findViewById(R.id.nav_view);
        hView =  navigationView.inflateHeaderView(R.layout.navigation_header);
        profile_image = hView.findViewById(R.id.profile_image);
        TextView display_name = (TextView)hView.findViewById(R.id.display_name);
        display_name.setText(mUsername);
        //drawer layout
        //Drawer Layout
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
                                startActivity(new Intent(HealthTips.this, UserLogin.class));
                                finish();
                                break;
                            case R.id.nav_favourites:
                                startActivity(new Intent(HealthTips.this, FavouritesActivity.class));
                                finish();
                                break;
                            case R.id.nav_home:
                                startActivity(new Intent(HealthTips.this, HomeActivity.class));
                                finish();
                                break;
                            case R.id.nav_health_tips:
                                startActivity(new Intent(HealthTips.this, HealthTips.class));
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


    }
}
