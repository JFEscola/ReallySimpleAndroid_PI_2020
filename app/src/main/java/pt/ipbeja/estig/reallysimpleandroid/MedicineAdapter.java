package pt.ipbeja.estig.reallysimpleandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.activities.medicinefeature.ChangeOrDeleteMedicineActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    Context context;
    List<Medicine> list;
    String person;

    public MedicineAdapter(Context context, List<Medicine> list, String person){
        this.context = context;
        this.list = list;
        this.person = person;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View medicineList = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list_item, parent , false);
       return new MyViewHolder(medicineList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Medicine medicine = list.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.medicineTime.setText(medicine.getTime());
        holder.medicineDayOfWeek.setText(getDayOfWeek(medicine));

        if(person.equals("admin")){
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChangeOrDeleteMedicineActivity.class);
                intent.putExtra("medicineName", medicine.getName());
                intent.putExtra("time", medicine.getTime());
                intent.putExtra("medicine", medicine);
                holder.itemView.getContext().startActivity(intent);

            });
        }
    }

    private String getDayOfWeek(Medicine medicine)
    {
        String dow = "";
        if (medicine.isMonday())
        {
            dow += "Seg ";
        }

        if (medicine.isTuesday())
        {
            dow += "Ter ";
        }

        if (medicine.isWednesday())
        {
            dow += "Qua ";
        }

        if (medicine.isThursday())
        {
            dow += "Qui ";
        }

        if (medicine.isFriday())
        {
            dow += "Sex ";
        }

        if (medicine.isSaturday())
        {
            dow += "Sáb ";
        }

        if (medicine.isSunday())
        {
            dow += "Dom";
        }

        return dow;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView medicineName;
        TextView medicineTime;
        TextView medicineDayOfWeek;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.textView_pill_name);
            medicineTime = itemView.findViewById(R.id.textView_pill_time);
            medicineDayOfWeek = itemView.findViewById(R.id.textView_day_week);
        }
    }
}
