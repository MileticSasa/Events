package com.cubes.miletic.events.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.ActivityManageAcountBinding;
import com.cubes.miletic.events.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ManageAcountActivity extends AppCompatActivity {

    private ActivityManageAcountBinding binding;

    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private StorageReference reference;

    private final String storagePath = "Users_Images/";

    private String[] storagePermissions;

    private User modelUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAcountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        reference = FirebaseStorage.getInstance().getReference();

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkStoragePermission()){
                    requestStoragePermission();
                }
                else{
                    pickImageFromGallery();
                }
            }
        });

        binding.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateName();
            }
        });

        binding.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSurname();
            }
        });

        binding.ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation();
            }
        });
    }

    private void updateName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update name?");
        builder.setCancelable(false);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);

        EditText et = new EditText(this);
        et.setHint(binding.tvName.getText().toString());

        layout.addView(et);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firestore.collection(Constants.TABLE_USER).document(user.getUid())
                        .update("name", et.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.tvName.setText(et.getText().toString());
                                dialogInterface.dismiss();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void updateSurname() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update surname?");
        builder.setCancelable(false);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);

        EditText et = new EditText(this);
        et.setHint(binding.tvSurname.getText().toString());

        layout.addView(et);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firestore.collection(Constants.TABLE_USER).document(user.getUid())
                        .update("surname", et.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.tvSurname.setText(et.getText().toString());
                                dialogInterface.dismiss();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void updateLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update location?");
        builder.setCancelable(false);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);

        EditText et = new EditText(this);
        et.setHint(binding.tvLocation.getText().toString());

        layout.addView(et);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firestore.collection(Constants.TABLE_USER).document(user.getUid())
                        .update("location", et.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.tvLocation.setText(et.getText().toString());
                                dialogInterface.dismiss();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserDataIntoViews();
    }

    private void uploadImage(Uri imageUri) {
        String filePath = storagePath + "image" + "_"+imageUri.getLastPathSegment();

        final StorageReference storageReference = reference.child(filePath);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        binding.pb.setVisibility(View.VISIBLE);

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());

                        if(uriTask.isSuccessful()){
                            Uri imageLocationInStorage = uriTask.getResult();

                            //save image path into firestore
                            firestore.collection(Constants.TABLE_USER).document(user.getUid())
                                    .update("image", imageLocationInStorage.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            binding.pb.setVisibility(View.GONE);

                                            try{
                                                Picasso.get().load(imageLocationInStorage).into(binding.imageView);
                                            }
                                            catch (Exception e){
                                                Picasso.get().load(R.drawable.ic_person).into(binding.imageView);
                                            }

                                            //remove old image after saving new one
                                            deleteOldImageFromStorage();

                                            Toast.makeText(ManageAcountActivity.this, "Image updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            binding.pb.setVisibility(View.GONE);
                                            Toast.makeText(ManageAcountActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.pb.setVisibility(View.GONE);
                        Toast.makeText(ManageAcountActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteOldImageFromStorage() {
        if(modelUser.image != null){
            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(modelUser.image);
            photoRef.delete();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.IMAGE_PICK_GALLERY_CODE){
            if(data != null){
                uploadImage(data.getData());

                modelUser.image = data.getData().toString();

                try {
                    Picasso.get().load(modelUser.image).into(binding.imageView);
                }
                catch (Exception e){
                    Picasso.get().load(R.drawable.ic_person).into(binding.imageView);
                }
            }
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, Constants.STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, R.string.storage_permission, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUserDataIntoViews() {
        binding.pb.setVisibility(View.VISIBLE);

        firestore.collection(Constants.TABLE_USER).document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        modelUser = documentSnapshot.toObject(User.class);

                        if(modelUser != null) {
                            try {
                                Picasso.get().load(modelUser.image).into(binding.imageView);
                            } catch (Exception e) {
                                Picasso.get().load(R.drawable.ic_person).into(binding.imageView);
                            }

                            binding.tvName.setText(modelUser.name);
                            binding.tvSurname.setText(modelUser.surname);
                            binding.tvLocation.setText(modelUser.location);
                        }
                    }
                });

        binding.pb.setVisibility(View.GONE);
    }
}