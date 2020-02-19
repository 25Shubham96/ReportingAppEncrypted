package com.example.shubhammundra.fromandto.SQLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.shubhammundra.fromandto.R;

import java.util.ArrayList;

public class SQLITEadapter extends BaseAdapter{

    private ArrayList<SQLITEpojo> data;

    private Context context;
    private TextViewOnClickListner textViewOnClickListner;
    private EditOnClickListner editOnClickListner;
    private DeleteOnClickListner deleteOnClickListner;

    private ImageView imageView;

    private LayoutInflater layoutInflater;

    SQLITEadapter(ArrayList<SQLITEpojo> data, Context context, TextViewOnClickListner textViewOnClickListner, EditOnClickListner editOnClickListner, DeleteOnClickListner deleteOnClickListner) {
        this.data = data;
        this.context = context;
        this.textViewOnClickListner = textViewOnClickListner;
        this.editOnClickListner = editOnClickListner;
        this.deleteOnClickListner = deleteOnClickListner;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int adapPos, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View view1 = layoutInflater.inflate(R.layout.database_option_dialog, viewGroup, false);

        TextView textView = view1.findViewById(R.id.CompName_DataBaseName);
        imageView = view1.findViewById(R.id.img_selectFunc);

        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ViewHolder")
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context,view);
                popup.getMenuInflater().inflate(R.menu.optionmenu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.EditOption:
                            {
                                SQLITEpojo sqlitEpojo = data.get(adapPos);

                                if (editOnClickListner != null)
                                {
                                    editOnClickListner.onClickItemEdit(sqlitEpojo);
                                }
                                break;
                            }

                            case R.id.DeleteOption:
                            {
                                SQLITEpojo sqlitEpojo = data.get(adapPos);

                                if (deleteOnClickListner != null)
                                {
                                    deleteOnClickListner.onClickItemDelete(sqlitEpojo);
                                }
                                break;
                            }

                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        textView.setText(String.valueOf(data.get(adapPos).getCompany() + ": " + data.get(adapPos).getName()));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLITEpojo sqlitEpojo = data.get(adapPos);

                if (textViewOnClickListner != null)
                {
                    textViewOnClickListner.onClickItem(sqlitEpojo);
                }

            }
        });

        return view1;
    }

    public interface TextViewOnClickListner { void onClickItem(SQLITEpojo sqlitEpojo);}
    public interface EditOnClickListner { void onClickItemEdit(SQLITEpojo sqlitEpojo);}
    public interface DeleteOnClickListner { void onClickItemDelete(SQLITEpojo sqlitEpojo);}
}
