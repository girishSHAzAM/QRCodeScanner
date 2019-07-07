package com.example.qrcodescanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class R_Adapter extends RecyclerView.Adapter<R_Adapter.R_Holder> {
    List<String>nameData,timeData,phoneData;
    List<Integer> idData;
    Context ct;
   public R_Adapter(Context context,List<Integer> idList, List<String> nameList,List<String>phoneList,List<String>timeList){
          ct = context;
          idData = idList;
          nameData = nameList;
          phoneData= phoneList;
          timeData = timeList;
   }
    @NonNull
    @Override
    public R_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflator = LayoutInflater.from(ct);
        View rView = inflator.inflate(R.layout.recycler_row,viewGroup,false);
        return new R_Holder(rView);
    }

    @Override
    public void onBindViewHolder(@NonNull R_Holder r_holder, int i) {
        r_holder.t1.setText(Integer.toString(idData.get(i)));
        r_holder.t2.setText(nameData.get(i));
        r_holder.t3.setText(phoneData.get(i));
        r_holder.t4.setText(timeData.get(i));
    }

    @Override
    public int getItemCount() {
        return nameData.size();
    }

    public class R_Holder extends RecyclerView.ViewHolder{
       TextView t1,t2,t3,t4;
        public R_Holder(View view){
            super(view);
            t1 = view.findViewById(R.id.id_number);
            t2 = view.findViewById(R.id.name);
            t3 = view.findViewById(R.id.phone);
            t4 = view.findViewById(R.id.time);
        }
    }
}
