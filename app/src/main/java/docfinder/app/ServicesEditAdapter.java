package docfinder.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ServicesEditAdapter extends RecyclerView.Adapter<ServicesEditAdapter.ViewHolder> {
    private List<ServicesList> myListList;
    private Context ct;


    public ServicesEditAdapter(List<ServicesList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.services_edit_rec,parent,false);
        return new ViewHolder(view);




    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServicesList myList=myListList.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
       holder.edit_serviceText.setText(myList.getService());


       holder.serviceRemove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               db.collection("Facility").document(MainActivity.currentViewId).collection("Services").document(myList.getServiceID())
                       .delete()
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {

                               MainActivity.edit_service_adapter = new ServicesEditAdapter(MainActivity.service_myLists, ct);
                               MainActivity.edit_service_rv.setAdapter( MainActivity.edit_service_adapter);
                               MainActivity.edit_service_adapter.notifyDataSetChanged();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       });

           }
       });




    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView edit_serviceText;
        private CardView serviceRemove;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            edit_serviceText = (TextView) itemView.findViewById(R.id.edit_serviceText);
            serviceRemove = (CardView) itemView.findViewById(R.id.serviceRemove);



        }
    }
}