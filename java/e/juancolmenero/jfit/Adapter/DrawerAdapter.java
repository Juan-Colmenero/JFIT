package e.juancolmenero.jfit.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.juancolmenero.jfit.Models.NavItemModel;
import e.juancolmenero.jfit.Models.UserWorkoutCategory;
import e.juancolmenero.jfit.Models.UserWorkoutSubcategory;
import e.juancolmenero.jfit.R;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerHolder> {
    Context context;
    ArrayList<NavItemModel> arrayList;

    public DrawerAdapter(Context context, ArrayList<NavItemModel> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public DrawerAdapter.DrawerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_panel,parent,false);
        return new DrawerAdapter.DrawerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrawerAdapter.DrawerHolder holder, final int position) {

        final NavItemModel navItemModel = arrayList.get(position);

        String title = navItemModel.getTitle();


        holder.nav_title.setText(title);
        holder.nav_icon.setImageResource(navItemModel.getIcon());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DrawerHolder extends RecyclerView.ViewHolder
    {
        TextView nav_title;
        ImageView nav_icon;

        public DrawerHolder(View itemView)
        {
            super(itemView);

            nav_title = itemView.findViewById(R.id.nav_title);
            nav_icon = itemView.findViewById(R.id.nav_icon);
        }

    }
}
