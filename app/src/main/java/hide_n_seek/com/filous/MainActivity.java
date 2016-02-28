package hide_n_seek.com.filous;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation = null;
   // private TextView mLatitudeText = null;
   // private TextView mLongitudeText = null;
    private TextView mName = null;
    private LocationRequest mLocationRequest = new LocationRequest();
    private AccessToken accessToken;
    // private TextView test2;
    private Firebase ref = null;
    private EditText mEdit;
    private String first_name = null;
    private String last_name = null;
    private String name = null;
    private String email = null;
    private String age_range = null;
    private String gender = null;
    private EditText interest1 = null;
    private EditText interest2 = null;
    private EditText interest3 = null;

    private LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Bundle extras = getIntent().getExtras();
            accessToken = AccessToken.getCurrentAccessToken();
            Log.v("Token",accessToken.toString());


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLatitudeText = (TextView)findViewById(R.id.LatitudeText);
       // mLongitudeText = (TextView)findViewById(R.id.LongitudeText);
        interest1 = (EditText) findViewById(R.id.Interest1);
        interest2 = (EditText) findViewById(R.id.Interest2);
        interest3 = (EditText) findViewById(R.id.Interest3);
        ref = new Firebase("https://blistering-torch-1778.firebaseio.com");
       // test2 = (TextView) findViewById(R.id.test);
        onFacebookAccessTokenChange(accessToken);
        getUserInfo(accessToken);

        Button clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String interest1_text = interest1.getText().toString();
                String interest2_text = interest2.getText().toString();
                String interest3_text = interest3.getText().toString();

                Firebase userRef = ref.child(name);

                userRef.child("interest1").setValue(interest1_text);
                userRef.child("interest2").setValue(interest2_text);
                userRef.child("interest3").setValue(interest3_text);

                userRef.child("latitude").setValue(String.valueOf(mLastLocation.getLatitude()) + " "+ String.valueOf(mLastLocation.getLongitude()));

                userRef.child("first").setValue(first_name);
                userRef.child("last").setValue(last_name);

                userRef.child("age_range").setValue(age_range);
                userRef.child("email").setValue(email);
                userRef.child("gender").setValue(gender);

                Intent i = new Intent(getBaseContext(), OutputActivity.class);
                startActivity(i);

            }
        });
    }
    private void getUserInfo(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code

                        try {
                            first_name = object.getString("first_name");
                            last_name = object.getString("last_name");
                            name = object.getString("name");
                            email = object.getString("email");
                            age_range = object.getString("age_range");
                            gender = object.getString("gender");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("first_name", first_name);
                        Log.v("last_name", last_name);
                        Log.v("email", email);
                        Log.v("birthday", age_range);
                        Log.v("gender", gender);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,name,email,age_range,gender");
        request.setParameters(parameters);
        request.executeAsync();


    }
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            ref.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // The Facebook user is now authenticated with your Firebase app
                   // test2.setText("Success");
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                   // test2.setText("Failure");
                }
            });
        } else {
        /* Logged out of Facebook so do a logout from the Firebase app */
            ref.unauth();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
    }

    @Override
    public void onLocationChanged(Location location) {


        mLastLocation = location;
       // updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }



    public void onSubmit() {
        String interest1_text = interest1.getText().toString();
        String interest2_text = interest2.getText().toString();
        String interest3_text = interest3.getText().toString();

        Firebase userRef = ref.child("Users").child(name);

        userRef.child("Interests").child("interest1").setValue(interest1_text);
        userRef.child("Interests").child("interest2").setValue(interest2_text);
        userRef.child("Interests").child("interest3").setValue(interest3_text);

        userRef.child("Location").child("latitude").setValue(String.valueOf(mLastLocation.getLatitude()));
        userRef.child("Location").child("longitude").setValue(String.valueOf(mLastLocation.getLongitude()));

        userRef.child("Name").child("first").setValue(first_name);
        userRef.child("Name").child("last").setValue(last_name);

        userRef.child("age_range").setValue(age_range);
        userRef.child("email").setValue(email);
        userRef.child("gender").setValue(gender);
    }
    private void updateUI() {

        //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
    }


}
