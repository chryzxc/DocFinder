package docfinder.app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.DataOutputStream;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {
    private List<NearbyList> myListList;
    private Context ct;


    public NearbyAdapter(List<NearbyList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_rec,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NearbyList myList=myListList.get(position);

        holder.nearby_name.setText(myList.getFacility_name());

        Toast.makeText(ct, String.valueOf(myList.getDistance()), Toast.LENGTH_SHORT).show();

        if (myList.getDistance() != null){

            if (myList.getDistance() < 1000){

                holder.nearby_distance.setText(String.format("%.2f", myList.getDistance()) +" (meters)");
            }else if (myList.getDistance() > 1000){
                holder.nearby_distance.setText(String.format("%.2f", myList.getDistance()) +" (kilometers)");
            }
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getId()+"/cover.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(ct).load(uri).centerCrop().placeholder(R.drawable.loading).dontAnimate().into(holder.nearby_photo);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });



/*




 */


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



       // holder.memberProf




    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView nearby_photo;
        private TextView nearby_name,nearby_distance;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nearby_name = (TextView) itemView.findViewById(R.id.nearby_name);
            nearby_distance = (TextView) itemView.findViewById(R.id.nearby_distance);
            nearby_photo = (ImageView) itemView.findViewById(R.id.nearby_photo);





        }
    }
}