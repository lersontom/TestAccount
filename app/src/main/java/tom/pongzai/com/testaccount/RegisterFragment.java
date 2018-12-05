package tom.pongzai.com.testaccount;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private MyAlert myAlert ;
    private DatabaseReference databaseReference;
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
            EditText editTextName = getView().findViewById(R.id.editTextName);
            EditText editTextEmail = getView().findViewById(R.id.editTextEmail);
            EditText editTextPassword = getView().findViewById(R.id.editTextPassword);


            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();

            myAlert = new MyAlert(getActivity());
            if(name.equals("")) {
                myAlert.normalDialog("Hey", "Please Check You Name");
                editTextName.requestFocus();

            }else if(email.equals("")) {
                myAlert.normalDialog("Hey", "Please Check You Email");
                editTextEmail.requestFocus();

            }else if(password.equals("") ) {
                myAlert.normalDialog("Hey", "Please Check You password");
                editTextPassword.requestFocus();

            }else if( password.length() < 4) {
                myAlert.normalDialog("Hey", "Please Check You password upper 4 charecters");
                editTextPassword.selectAll();
                editTextPassword.requestFocus();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createToolbar() {
        Toolbar toolbar  = getView().findViewById(R.id.toolsbarRegister);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
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
