package docfinder.app;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Users {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void displayUsers(Context context) {



        db.collection("Users").orderBy("name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()){

                    MainActivity.users_myLists.clear();
                    MainActivity.users_adapter = new UsersAdapter(MainActivity.users_myLists, context);
                    MainActivity.users_rv.setAdapter(MainActivity.users_adapter);
                    MainActivity.users_adapter.notifyDataSetChanged();


                    if (error != null) {
                        Toast.makeText(context, "Listen failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (firebaseAuth.getCurrentUser() != null) {
                    }
                    for (DocumentSnapshot document : snapshots.getDocuments()) {

                        if (document.exists()){
                            MainActivity.users_myLists.add(new UsersList(document.getId()));

                        }


                    }

                    MainActivity.users_adapter = new UsersAdapter(MainActivity.users_myLists, context);
                    MainActivity.users_rv.setAdapter(MainActivity.users_adapter);
                    MainActivity.users_adapter.notifyDataSetChanged();

                }

            }
        });


    }

}
