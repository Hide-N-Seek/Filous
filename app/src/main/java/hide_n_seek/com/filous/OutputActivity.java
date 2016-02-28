package hide_n_seek.com.filous;



        import android.app.ListActivity;
        import android.content.SharedPreferences;
        import android.database.DataSetObserver;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.AccessToken;
        import com.facebook.FacebookSdk;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;
        import com.firebase.ui.FirebaseListAdapter;
        import com.firebase.ui.auth.core.AuthProviderType;
        import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
        import com.firebase.ui.auth.core.FirebaseLoginError;

        import java.util.Random;

public class OutputActivity extends FirebaseLoginBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        ListView OutputList = (ListView) findViewById(R.id.outputList);

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://blistering-torch-1778.firebaseio.com");

        mAdapter = new FirebaseListAdapter<Users>(this, Users.class, android.R.layout.two_line_list_item, ref) {
            @Override
            protected void populateView(View view, Users users, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(users.getFirst());
                ((TextView)view.findViewById(android.R.id.text2)).setText(users.getLatitude());

            }
        };
        OutputList.setAdapter(mAdapter);
    }
    private FirebaseListAdapter mAdapter;

    @Override
    public Firebase getFirebaseRef() {
        // TODO: Return your Firebase ref
        Firebase ref = new Firebase("https://blistering-torch-1778.firebaseio.com");
        return ref;
    }

    @Override
    public void onFirebaseLoginProviderError (FirebaseLoginError firebaseError){
        // TODO: Handle an error from the authentication provider
    }

    @Override
    public void onFirebaseLoginUserError (FirebaseLoginError firebaseError){
        // TODO: Handle an error from the user
    }

    @Override
    protected void onStart() {
        super.onStart();
        // All providers are optional! Remove any you don't want.
        setEnabledAuthProvider(AuthProviderType.FACEBOOK);
    }
    private AccessToken accessToken;
}


