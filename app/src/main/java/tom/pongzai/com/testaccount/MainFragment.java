package tom.pongzai.com.testaccount;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private DatabaseReference databaseReference;
    private TextView editTextEmailLogIn;
    private TextView editTextPasswordLogIn;
    private Button buttonSignUp;
    private Button buttonSigin;
    private CheckBox checkBoxRememberEmail;
    private CheckBox checkBoxRememberPassword;
    MyAlert alertDialog;
    private SharedPreferences sharedPreferences;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        checkAuten

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getUid() != null)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment, new ServiceFragment()).commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editTextEmailLogIn = getView().findViewById(R.id.editTextEmailLogIn);
        editTextPasswordLogIn = getView().findViewById(R.id.editTextPasswordLogIn);
        checkBoxRememberEmail = getView().findViewById(R.id.checkboxRememberEmail);
        checkBoxRememberPassword = getView().findViewById(R.id.checkboxRememberPassword);
//        signup controller


        signUpContoller();
        singInController();


    }//main method


    public void singInController() {
        Button buttonSingIn = getView().findViewById(R.id.buttonSignIn);
        buttonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailLogIn.getText().toString();
                String password = editTextPasswordLogIn.getText().toString();

                final MyAlert myAlert = new MyAlert(getActivity());
                if (email.isEmpty() || password.isEmpty()) {
                    myAlert.normalDialog(getString(R.string.title_have_space) , getString(R.string.message_have_space));
                } else {

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment, new ServiceFragment()).commit();
                            } else {
                                myAlert.normalDialog("hey", task.getException().toString());
                            }

                        }
                    });

                } // if


            }
        });
    }


    public void signUpContoller() {

        buttonSignUp = getView().findViewById(R.id.buttonSignup);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
// getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment,
//                        new RegisterFragment()).commit();
// Replace Flagment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment,
                        new RegisterFragment()).addToBackStack(null).commit();

            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
