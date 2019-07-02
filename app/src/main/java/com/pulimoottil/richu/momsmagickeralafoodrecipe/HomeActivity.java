package com.pulimoottil.richu.momsmagickeralafoodrecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    //cardview
    CardView biriyani,noodles,friedrice,beefpork;
    //end of cardview
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser;
    String mEmail,mUsername,mId,mPhoto;
    Uri mPhotoUri;
    NavigationView navigationView;
    View hView;
    CircleImageView profile_image;

    private static long back_pressed;



//imgvw .setImageResource();

    //app:headerLayout="@layout/navigation_header"
    //TextView display_name = (TextView)hView.findViewById(R.id.display_name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //fab
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GroceryListActivity.class);
                startActivity(intent);
            }
        });
        //cardview
        biriyani = findViewById(R.id.cdBiriyani);
        noodles = findViewById(R.id.cdNoodles);
        beefpork = findViewById(R.id.cdBeefPork);
        friedrice = findViewById(R.id.cdFriedRice);
        biriyani.setOnClickListener(this);
        noodles.setOnClickListener(this);
        beefpork.setOnClickListener(this);
        friedrice.setOnClickListener(this);
        //end of cardview
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

        //display_name = findViewById(R.id.display_name);
        checkUserInDatabase();
        //Drawer Layout
        mDrawerLayout = findViewById(R.id.drawerLayout);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(HomeActivity.this, UserLogin.class));
                                finish();
                                break;
                            case R.id.nav_favourites:
                                startActivity(new Intent(HomeActivity.this, FavouritesActivity.class));
                                finish();
                                break;
                            case R.id.nav_home:
                                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                                finish();
                                break;
                            case R.id.nav_health_tips:
                                startActivity(new Intent(HomeActivity.this, HealthTips.class));
                                finish();
                                break;
                                default:
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return false;
                    }
                });
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    private void checkUserInDatabase(){
        ref.child("users").child(mId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // use "username" already exists
                    // Let the user know he needs to pick another username.
                } else {
                    Toast.makeText(HomeActivity.this, "Your Account has been registered", Toast.LENGTH_SHORT).show();
                        ref.child("users").child(mId).child("email").setValue(mEmail);
                        ref.child("users").child(mId).child("username").setValue(mUsername);
                        ref.child("users").child(mId).child("photourl").setValue(mPhoto);

                    // User does not exist. NOW call createUserWithEmailAndPassword
                    // Your previous code here.
                }

                Picasso.get().load(mPhotoUri).into(profile_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) finish();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){

            switch (item.getItemId()) {
                case R.id.nav_logout:
                    Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, UserLogin.class));
                    return true;
                case R.id.nav_home:
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                    finish();
                    break;
                case R.id.nav_health_tips:
                    Toast.makeText(this, "Health Tips", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(HomeActivity.this, HealthTips.class));
                    //finish();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String selectedCategory;
        Intent intent = new Intent(this, CategorizedItems.class);
        switch (v.getId()){
            case R.id.cdBiriyani:
                selectedCategory = "Biriyani";
                intent.putExtra("selectedCategory",selectedCategory);
                startActivity(intent);
                break;
            case R.id.cdBeefPork:
                selectedCategory = "BeefPork";
                intent.putExtra("selectedCategory",selectedCategory);
                startActivity(intent);
                break;
            case R.id.cdFriedRice:
                selectedCategory = "FriedRice";
                intent.putExtra("selectedCategory",selectedCategory);
                startActivity(intent);
                break;
            case R.id.cdNoodles:
                selectedCategory = "Noodles";
                intent.putExtra("selectedCategory",selectedCategory);
                startActivity(intent);
        }
    }
}
