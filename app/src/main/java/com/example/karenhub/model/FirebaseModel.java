package com.example.karenhub.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseModel {
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser CurrUser;

    FirebaseModel() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    public void getAllPosts(Model.Listener<List<Post>> callback) {
        db.collection(Post.COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> list = new LinkedList<>();
                if (task.isSuccessful()) {
                    QuerySnapshot jsonsList = task.getResult();
                    for (DocumentSnapshot json : jsonsList) {
                        Post post = Post.fromJson(json.getData());
                        list.add(post);
                    }
                }
                if (list != null) {
                    Collections.sort(list, (p1, p2) ->
                            Long.compare(p2.getTimestamp(), p1.getTimestamp()));
                }
                callback.onComplete(list);
            }
        });
    }

    public void addPost(Post post, Model.Listener<Void> listener) {
        db.collection(Post.COLLECTION).document(post.getId()).set(post.toJson())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(null);
                    }
                });
    }

    public void getPostById(String id, Model.Listener<Post> listener) {
        db.collection(Post.COLLECTION).whereEqualTo(Post.ID, id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Post post = new Post();
                if (task.isSuccessful()) {
                    QuerySnapshot jsonsList = task.getResult();
                    for (DocumentSnapshot json : jsonsList) {
                        post = Post.fromJson(json.getData());

                    }
                }
                listener.onComplete(post);
            }
        });
    }

    public void updatePostByid(String id, Map<String, Object> updates){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("map",updates.toString());
        CollectionReference collRef = db.collection("posts");
        collRef.whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG1", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG2", "Error updating document", e);
                                        }
                                    });
                        }
                    }
                });
    }

    void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }

    public void signUp(String email, String label, String password, Model.Listener<Pair<Boolean, String>> listener) {
        db.collection(User.COLLECTION).whereEqualTo(User.ACCOUNT_LABEL, label).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Add the new user to the database
                                            User user = new User(email, label);
                                            db.collection(User.COLLECTION).add(user.toJson()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                    listener.onComplete(new Pair<>(true, "Sign up success"));
                                                }
                                            });
                                        } else {
                                            Exception exception = task.getException();
                                            if (exception instanceof FirebaseAuthUserCollisionException) {
                                                // Email already exists
                                                listener.onComplete(new Pair<>(false, "Email already exists"));
                                            } else {
                                                // Other error like connection issue or password under 6 characters / invalid characters
                                                listener.onComplete(new Pair<>(false, "Sign up failed"));
                                            }
                                        }
                                    }
                                });
                    } else {

                        listener.onComplete(new Pair<>(false, "Label is taken"));
                    }
                } else {

                    listener.onComplete(new Pair<>(false, "Error checking for unique label"));
                }
            }
        });
    }

    public void login(String email, String password, Model.Listener<Pair<Boolean, String>> listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    CurrUser = auth.getCurrentUser();
                    listener.onComplete(new Pair<>(true, "Logged in successfully"));
                } else {
                    listener.onComplete(new Pair<>(false, "Login failed"));
                }
            }
        });
    }


    public FirebaseFirestore getDb() {
        return db;
    }

    public void getUserPosts(String label, Model.Listener<List<Post>> callback) {
        db.collection(Post.COLLECTION)
                .whereEqualTo("label", label)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()) {
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json : jsonsList) {
                                Post post = Post.fromJson(json.getData());
                                list.add(post);
                            }
                        }
                        if (list != null) {
                            Collections.sort(list, (p1, p2) ->
                                    Long.compare(p2.getTimestamp(), p1.getTimestamp()));
                        }
                        callback.onComplete(list);
                    }
                });
    }

    public void signOut() {
        auth.signOut();
    }
}
