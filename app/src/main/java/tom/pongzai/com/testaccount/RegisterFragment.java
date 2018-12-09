package tom.pongzai.com.testaccount;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    //    Explicit
    private boolean avartaABoolean = true;
    private ImageView imageView;
    private Uri uri;
    private String tag = "17Nov";
    private MyAlert myAlert;
    private DatabaseReference databaseReference;

    private String nameString, emailString, passwordString, uavatarString, uidString;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myAlert = new MyAlert(getActivity());

//      Create Toolbar
        createToolbar();
//      avatar Controller
        avatarController();


    }// main method

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            avartaABoolean = false;
            uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 600, false);
                imageView.setImageBitmap(bitmap1);


            } catch (Exception e) {
                Log.d(tag, "e==> " + e.toString());
            }
        }
    }

    private void avatarController() {
        imageView = getView().findViewById(R.id.imageViewAvatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent to gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose..."), 1);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemUpload) {

            checkDataAndUpload();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkDataAndUpload() {
        myAlert = new MyAlert(getActivity());
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Upload Avatar Image");
        progressDialog.setMessage("Please wait few minus...");


        // get value from
        EditText editTextName = getView().findViewById(R.id.editTextName);
        EditText editTextEmail = getView().findViewById(R.id.editTextEmail);
        EditText editTextPassword = getView().findViewById(R.id.editTextPassword);

        nameString = editTextName.getText().toString().trim();
        emailString = editTextEmail.getText().toString().trim();
        passwordString = editTextPassword.getText().toString();


        if (avartaABoolean) {
            myAlert.normalDialog("Non Choose Image", "Please Choose Image");
        } else if (nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty()) {
            myAlert.normalDialog(getString(R.string.title_have_space), getString(R.string.message_have_space));
        } else {
            progressDialog.show();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            storageReference.child("avatar/" + nameString).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Success Upload", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    findPathAvatar();
                    register();

                }
            });

        }

    }

    private void register() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                   updateDatabase();
                else
                    myAlert.normalDialog("Cannot regis", task.getException().toString());
            }
        });
    }

    private void updateDatabase() {
//        Find uIdFind
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        uidString = firebaseAuth.getUid();
        Log.d("9DecV1", "ustring=>"+uidString);


//        save in database
        UserModel userModel = new UserModel(uavatarString, uidString,  "Hello Android", nameString);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(uidString);
        databaseReference.setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(getActivity().getSupportFragmentManager().popBackStackImmediate()); // back to loin
            }
        });



    }

    private void findPathAvatar() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("avatar").child(nameString);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uavatarString = uri.toString();
                Log.d("9DecV1", "avatar => " + uavatarString);
            }
        });
    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolsbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Register");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Please .......");
//        navigater icon
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

}
