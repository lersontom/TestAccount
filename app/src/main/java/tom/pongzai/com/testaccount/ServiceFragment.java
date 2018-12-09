package tom.pongzai.com.testaccount;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {


    private String currentUID;

    public ServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findUID();

        // Create tools bar
        createToolsBar();

//        post control
        postControl();





    }

    public void findUID() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUID = firebaseAuth.getUid();
    }

    public void postControl() {
        Button button = getView().findViewById(R.id.buttonPost);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = getView().findViewById(R.id.editTextMessage);
                final String message = editText.getText().toString().trim();
                if(message.isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.message_have_space), Toast.LENGTH_SHORT).show();

                }else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(currentUID);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
//                            (String uavatarString, String uidString, String umessageString, String unameString
                            UserModel userModel1 = new UserModel(userModel.getUavatarString(),
                                                                    userModel.getUidString(),
                                                                    message,
                                                                    userModel.getUnameString());
                            replaceMessgae(userModel1);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    editText.setText("");
                } //if
            }
        });
    }

    private void replaceMessgae(UserModel userModel1) {


        FirebaseDatabase  firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(currentUID);
        databaseReference.setValue(userModel1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Update message is O.K.", Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if( item.getItemId() == R.id.itemExit){
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_service, menu);
    }

    private void createToolsBar() {
        Toolbar toolbar=  getView().findViewById(R.id.toolsbarService);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_, container, false);
    }

}
