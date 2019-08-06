package chadaporn.piaras.kku.ac.th.audemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jaeger.library.StatusBarUtil;

public class SettingFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        String[] settingArray = { "Link Account", "Account", "Wallet", "Privacy", "Notification", "Log out" };

        ListView listView1 = (ListView) view.findViewById(R.id.listview_setting);
        listView1.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, settingArray));
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0
                    , View arg1, int arg2, long arg3) {
                Intent intent;
                switch(arg2) {
                    case 0 :
                        intent = new Intent(getActivity().getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                        break;
                    case 5 :
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setCancelable(false);
                        dialog.setTitle("");
                        dialog.setMessage("Do you really want to log out?" );
                        dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPrefManager.getInstans(getActivity().getApplicationContext()).logout();
                                getActivity().getFragmentManager().popBackStack();
                                startActivity(new Intent(getActivity(), LoginActivity.class));

                            }
                        });
                        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        final AlertDialog alert = dialog.create();
                        alert.show();

                        break;
                }
            }
        });

        return view;
    }


}
