package com.example.karenhub.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public static Model instance(){
        return _instance;
    }
    private Model(){

    }

    public interface Listener<T>{
        void onComplete(T data);
    }
    public FirebaseAuth getAuth() {
        return firebaseModel.auth;
    }


    public void getAllPosts(Listener<List<Post>> callback){
        firebaseModel.getAllPosts(callback);
//        executor.execute(()->{
//            List<Student> data = localDb.studentDao().getAll();
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->{
//                callback.onComplete(data);
//            });
//        });
    }

    public void addPost(Post st, Listener<Void> listener){
        firebaseModel.addPost(st,listener);
//        executor.execute(()->{
//            localDb.studentDao().insertAll(st);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }

    public void uploadImage(String name, Bitmap bitmap,Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }
     public void signUp(String email,String label,String password,Listener<Pair<Boolean,String>> listener){
        firebaseModel.signUp(email,label,password,listener);
     }
     public void login(String email,String password,Listener<Pair<Boolean,String>> listener){
        firebaseModel.login(email,password,listener);
     }


     public FirebaseFirestore getDb(){
        return firebaseModel.getDb();
     }


}
