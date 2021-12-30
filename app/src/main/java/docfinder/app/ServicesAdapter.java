package docfinder.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    private List<ServicesList> myListList;
    private Context ct;


    public ServicesAdapter(List<ServicesList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.services_rec,parent,false);
        return new ViewHolder(view);




    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServicesList myList=myListList.get(position);

       holder.serviceText.setText("• "+myList.getService());




    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView serviceText;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceText = (TextView) itemView.findViewById(R.id.serviceText);



        }
    }
}