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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editTextEmailLogIn = getView().findViewById(R.id.editTextEmailLogIn);
        editTextPasswordLogIn = getView().findViewById(R.id.editTextPasswordLogIn);
        checkBoxRememberEmail = getView().findViewById(R.id.checkboxRememberEmail);
        checkBoxRememberPassword = getView().findViewById(R.id.checkboxRememberPassword);
//        signup controller
        buttonSigin = getView().findViewById(R.id.buttonSignIn);
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


        buttonSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new MyAlert(getActivity());

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String email = dataSnapshot.child("email").getValue().toString();
                                String password = dataSnapshot.child("password").getValue().toString();
                                String messageEmail = " != ";
                                String messagePassword = " != ";

                                if(email.compareTo(editTextEmailLogIn.getText().toString()) == 0)
                                    messageEmail = " = ";
                                if(editTextPasswordLogIn.getText().toString().compareTo(password) == 0)
                                    messagePassword = " = ";
                                alertDialog.normalDialog("Login Pass",
                                        editTextEmailLogIn.getText()+ messageEmail +email
                                                +"\n"+
                                                editTextPasswordLogIn.getText()+messagePassword + password
                                );
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                alertDialog.normalDialog("Hey",databaseError.getMessage());
                            }
                        }
                );

            }
        });

        sharedPreferences = getContext().getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        checkBoxRememberEmail.setChecked(sharedPreferences.getBoolean("is_remember_email", false));
        checkBoxRememberPassword.setChecked(sharedPreferences.getBoolean("is_remember_password", false));
        if(checkBoxRememberEmail.isChecked())
            editTextEmailLogIn.setText(sharedPreferences.getString("email", ""));
        if(checkBoxRememberPassword.isChecked())
            editTextPasswordLogIn.setText(sharedPreferences.getString("password", ""));

    }//main method



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroy() {
        Editor editor = sharedPreferences.edit();

        if(!checkBoxRememberEmail.isChecked()) {
            editor.clear();
            checkBoxRememberPassword.setChecked(false);
        }else{
            if(checkBoxRememberEmail.isChecked())
                editor.putString("email", editTextEmailLogIn.getText().toString().trim());
            if(checkBoxRememberPassword.isChecked())
                editor.putString("password", editTextPasswordLogIn.getText().toString().trim());
        }

        editor.putBoolean("is_remember_email", checkBoxRememberEmail.isChecked());
        editor.putBoolean("is_remember_password", checkBoxRememberPassword.isChecked());
        editor.commit();

        super.onDestroy();

    }
}
