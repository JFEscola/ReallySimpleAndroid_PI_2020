package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import pt.ipbeja.estig.reallysimpleandroid.R;

public class ContactPickDialog extends AppCompatDialogFragment
{
    private TextView pickContactListBtn;
    private TextView pickManualBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_pick_sos_dialog, null);

        builder.setView(view)
                .setTitle("Escolher Contacto").setNegativeButton("cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        this.pickContactListBtn = view.findViewById(R.id.pick_contact_list);
        this.pickManualBtn = view.findViewById(R.id.pick_write_manually);

        this.pickContactListBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getContext(), ChooseSOSContactActivity.class));
            }
        });

        return builder.create();
    }
}
