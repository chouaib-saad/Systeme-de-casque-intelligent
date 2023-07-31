package com.example.casquenet.main_screen_fragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.casquenet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserProfile extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseUser Currentuser;

    private ImageView userPicture;
    private ImageView buttRetour;
    private ImageView buttLogout;

    private LinearLayout modifyPassword_btn;


    private ProgressDialog progressDialog;


    private AlertDialog alertDialog;



    private static final int MY_PERMISSIONS_REQUEST_CAMERA_AND_STORAGE = 463;
    private static final int RESULT_OK = -1 ;

    //camera needed variables
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE_REQUEST=1;


    private Uri mImageUri;
    public String photoUrl;

    private TextView  userName,userId,userEmail,userPhone,userBirthday;


    //picture
    FirebaseFirestore db ;
    FirebaseStorage storage ;
    StorageReference storageRef ;
    DocumentReference userRef ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        userPicture = view.findViewById(R.id.userPicture);
        userName = view.findViewById(R.id.userName);
        userId = view.findViewById(R.id.userId);
        userEmail = view.findViewById(R.id.userEmail);
        userPhone = view.findViewById(R.id.userPhone);
        userBirthday = view.findViewById(R.id.userBirthday);



        //picture
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        progressDialog = new ProgressDialog(getContext());



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Currentuser = firebaseAuth.getCurrentUser();

        buttRetour = view.findViewById(R.id.buttRetour);

        buttLogout = view.findViewById(R.id.buttLogout);

        //modifier le mot de passe
        modifyPassword_btn = view.findViewById(R.id.modifyPassword_btn);


        //back button check
        if (!(requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)) {
            buttRetour.setVisibility(View.INVISIBLE);
        }


        if (Currentuser != null) {


            reference = firebaseDatabase.getReference().child("Users").child(Currentuser.getUid());

            // Read user data from Firebase
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nameS = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    String emailS = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    String idS = Objects.requireNonNull(snapshot.child("id").getValue()).toString();
                    String phoneS = Objects.requireNonNull(snapshot.child("phone").getValue()).toString();
                    String birthdateS = Objects.requireNonNull(snapshot.child("birthdate").getValue()).toString();

                    // Assign values to text fields
                    userName.setText(nameS);
                    userEmail.setText(emailS);
                    userId.setText(idS);
                    userPhone.setText("+216 "+phoneS);
                    userBirthday.setText(birthdateS);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Erreur lors de la récupération des données utilisateur", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{

            Toast.makeText(getActivity(), "utilisateur ne pas existe", Toast.LENGTH_SHORT).show();

        }




        /*

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
                id.setFocusableInTouchMode(true);
                phone.setFocusableInTouchMode(true);
                birthdate.setFocusableInTouchMode(true);
                edit.setText("Save");
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference.child("name").setValue(name.getText().toString());
                        reference.child("email").setValue(email.getText().toString());
                        reference.child("id").setValue(id.getText().toString());
                        reference.child("phone").setValue(phone.getText().toString());
                        reference.child("birthdate").setValue(birthdate.getText().toString());

                        Toast.makeText(getActivity(), "Your data has been changed successfully", Toast.LENGTH_SHORT).show();
                        name.clearFocus();
                        email.clearFocus();
                        id.clearFocus();
                        phone.clearFocus();
                        birthdate.clearFocus();
                        startActivity(new Intent(getActivity(), Maps.class));
                    }
                });
            }
        });

         */


        buttRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunction();
            }
        });


        buttLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogout();




            }
        });




        userPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePictureDialog();
            }
        });




        modifyPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangePasswordDialog();

            }
        });


        chargerProfilePicture(view);

            return view;




    }  ///////

























    private void userLogout() {

        //deconnect the user
         FirebaseAuth.getInstance().signOut();

        //desable the remember me button
        SharedPreferences preferences=requireActivity().getSharedPreferences("checkBox",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("remember","false");
        editor.apply();

        progressDialog.setMessage("deconnexion..");
        progressDialog.setCancelable(false);
        progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        requireActivity().finish();
                    }
                },2500);
    }






    private void backFunction(){


        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Create the instance of the fragment you want to open
            Fragment mainFragment = new Settings();

            // Perform the exit transition animation
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.fragmentContainer, mainFragment); // Replace with the main fragment
            transaction.commit();

            // Delay the onBackPressed() call to allow the animation to finish
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requireActivity().onBackPressed(); // Navigate to the previous fragment or activity
                }
            }, 200); // Adjust the delay time as needed

        }

    }




    public void showChangePasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.RoundedDialogStyle);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.change_password_dialog, null);
        builder.setView(view);

        EditText current_passwordEditText = view.findViewById(R.id.current_password);
        EditText newPasswordEditText = view.findViewById(R.id.new_password_edittext);
        EditText confirmPasswordEditText = view.findViewById(R.id.confirm_password_edittext);
        TextView titleTextView = view.findViewById(R.id.dialog_title_textview);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        // Set the title of the dialog
        titleTextView.setText("Nouveau mot de passe");

        // Set the click listener for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when Cancel is clicked
                alertDialog.dismiss();
            }
        });

        // Set the click listener for the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new password and confirm password from the EditText fields
                String current_password = current_passwordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if(current_password.isEmpty()){
                    current_passwordEditText.setError("Veuillez entrer votre mot de passe actuel");
                    current_passwordEditText.requestFocus();
                }
                else if (newPassword.isEmpty()) {
                    newPasswordEditText.setError("Veuillez entrer un nouveau mot de passe");
                    newPasswordEditText.requestFocus();
                } else if (confirmPassword.isEmpty()) {
                    confirmPasswordEditText.setError("Veuillez confirmer le mot de passe");
                    confirmPasswordEditText.requestFocus();
                } else if (!newPassword.equals(confirmPassword)) {
                    confirmPasswordEditText.setError("Les mots de passe ne correspondent pas");
                    confirmPasswordEditText.requestFocus();
                } else {
                    // The passwords match and are not empty
                    // Proceed with further actions (e.g., update the password in the database)

                    showProgressDialog(requireContext(),"attendez..",true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            changePassword(current_password,newPassword);

                        }
                    },1500);


                    // Dismiss the dialog
                    alertDialog.dismiss();
                }

            }
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }




    private void changePassword(String currentPassword, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Create a credential with the user's email and current password
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            // Reauthenticate the user with the provided credential
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> reauthTask) {
                            if (reauthTask.isSuccessful()) {
                                // User has been successfully reauthenticated

                                // Update the password
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Password updated successfully
                                                    Toast.makeText(requireContext(), "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                                    showProgressDialog(requireContext(), "attendez..", false);
                                                } else {
                                                    // An error occurred while updating the password
                                                    Toast.makeText(requireContext(), "Échec de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show();
                                                    showProgressDialog(requireContext(), "attendez..", false);
                                                }
                                            }
                                        });
                            } else {
                                // Reauthentication failed, show an error message
                                Toast.makeText(requireContext(), "Échec de l'authentification. Vérifiez votre mot de passe actuel.", Toast.LENGTH_SHORT).show();
                                showProgressDialog(requireContext(), "attendez..", false);
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "l'utilisateur ne pas existe", Toast.LENGTH_SHORT).show();
        }
    }





    //progressbar
    private void showProgressDialog(Context context, String message, boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }




    private void changePictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialogStyle);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_picture_layout, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create(); // Move the alertDialog initialization here

        TextView fromGalleryTextView = dialogView.findViewById(R.id.fromGalleryTextView);
        TextView fromCameraTextView = dialogView.findViewById(R.id.fromCameraTextView);

        fromGalleryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "From Gallery" option
                // Add your code here
                pickImageFromGallery();
                alertDialog.dismiss();
            }
        });

        fromCameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "From Camera" option
                // Add your code here
                pickImageFromCamera();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }





    private void pickImageFromGallery() {

        ImagePicker.with(UserProfile.this)
                .galleryOnly()
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }



    //drom Camera function
    private void pickImageFromCamera() {

        ImagePicker.with(UserProfile.this)
                .cameraOnly()
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }


    private void requestCameraPermission() {

        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return res1 && res2;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && data != null && data.getData() != null) {

            //URI of the image
            mImageUri = data.getData();

            if( mImageUri != null){


                showProgressDialog(getContext(),"",true);


                //the URI of the image file
                Uri fileUri = mImageUri;
                FirebaseFirestore fStore;




                            //StorageReference imageRef = storageRef.child("userVerificationDocs/" + userId + ".jpg");
                            String userUidPath = getUserUid() +"/";


                            StorageReference imageRef = storageRef.child("photo_profil/" + userUidPath + "image.jpg");

                            imageRef.putFile(fileUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Log.d("picture uploading", "Image uploaded successfully");


                                            // Get the download URL of the image and add it to the user document
                                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {


                                                    //save the user data on the current user Uid
                                                    DocumentReference df = db.collection("utilisateurs").document(getUserUid());


                                                    Map<String,Object> profilePic = new HashMap<>();
                                                    profilePic.put("photo_de_profil",uri.toString());

                                                    //create a new collection within the user document called "VerificationData"
                                                    //save verification data to the user doc

                                                    df.set(profilePic, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {


                                                                    //on profile picture changed listener here and loading progbar or with picasso
                                                                    loadProfilPicture(uri.toString());
                                                                    Toast.makeText(getContext(), "ajoute avec succes", Toast.LENGTH_SHORT).show();



                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {


                                                                    Toast.makeText(getContext(), "erreur", Toast.LENGTH_SHORT).show();
                                                                    showProgressDialog(getContext(),"",false);

                                                                }
                                                            });











                                                }  //image uploaded and get the image url successfully
                                            });



                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.d("image", "erreur d'ajout d'image");

                                            showProgressDialog(getContext(),"",false);



                                        }
                                    });










            }else{

                Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
                showProgressDialog(getContext(),"",false);

            }


        }
    }






    private void loadProfilPicture(String profilPictureUrl){

        Picasso.get()
                .load(profilPictureUrl)
                //.fit()
                .centerCrop()
                .placeholder(R.drawable.profile_avatar)
                .error(R.drawable.arierre)
                .resize(1600, 1600)
                .into(userPicture, new Callback() {

                    @Override
                    public void onSuccess() {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                showProgressDialog(getContext(),"",false);

                            }
                        }, 2000);


                    }

                    @Override
                    public void onError(Exception e) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                showProgressDialog(getContext(),"",false);

                            }
                        }, 2000);


                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }




    private void chargerProfilePicture(View v){


        userPicture = v.findViewById(R.id.userPicture);


        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        db.collection("utilisateurs")
                .document(getUserUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        String userPictureFromDB = documentSnapshot.getString("photo_de_profil");


                        if (userPictureFromDB == null) {
                            Log.d("TAG", "utilisateur n'existe pas");
                        }

                        //picasso function here
                        loadProfilPicture(userPictureFromDB);

                    }
                });


    } //setProfile data methode




    //get the user Uid
    private String getUserUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return currentUser.getUid();
    }


}   ////
