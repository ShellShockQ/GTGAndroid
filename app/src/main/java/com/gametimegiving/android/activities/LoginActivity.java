package com.gametimegiving.android.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//import com.appsee.Appsee;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.gametimegiving.android.Helpers.Utilities;
import com.gametimegiving.android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends GTGBaseActivity {
    private static final int RC_SIGN_IN = 777;
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // Appsee.start();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gametimegiving.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Utilities.GenerateHashKey(this);
        // setContentView(R.layout.activity_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            SaveUserLocal(auth.getCurrentUser());
//            userId = auth.getCurrentUser().getUid();
//            name = auth.getCurrentUser().getDisplayName();
//            Bundle bundle = new Bundle();
//            bundle.putString("user", userId);
//            bundle.putString("username", name);
//            if (photoUrl != null) {
//                bundle.putString("photoUrl", photoUrl.toString());
//            } else {
//                bundle.putString("photoUrl", "");
//            }
            Intent intent = new Intent(this, GameBoardActivity.class);
//            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.GTGAppTheme)
                            .setTosAndPrivacyPolicyUrls(getString(R.string.termsofserviceurl),
                                    getString(R.string.privacypolicyurl))
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    //new AuthUI.IdpConfig.TwitterBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                    RC_SIGN_IN);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                SaveUserLocal(firebaseUser);
//                Bundle bundle = new Bundle();
//                bundle.putString("user", firebaseUser.getUid());
//                bundle.putString("username", firebaseUser.getDisplayName());
//                if (photoUrl != null) {
//                    bundle.putString("photoUrl", firebaseUser.getPhotoUrl().toString());
//                } else {
//                    bundle.putString("photoUrl", "");
//                }
                Intent intent = new Intent(this, GameBoardActivity.class);
//                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
