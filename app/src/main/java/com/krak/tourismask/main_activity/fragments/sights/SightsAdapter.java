package com.krak.tourismask.main_activity.fragments.sights;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.krak.tourismask.R;
import com.krak.tourismask.entities.Sight;
import com.krak.tourismask.main_activity.MainActivity;
import com.krak.tourismask.sight_activities.EditSightActivity;
import com.krak.tourismask.utils.CustomAnimations;
import com.krak.tourismask.view_models.CurrentPositionViewModel;
import com.krak.tourismask.view_models.SightsViewModel;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;

import java.util.List;

public class SightsAdapter extends RecyclerView.Adapter<SightsAdapter.SightHolder> {

    private List<Sight> sights;
    private LayoutInflater inflater;
    private FragmentActivity activity;

    public SightsAdapter(FragmentActivity context, List<Sight> sights) {
        this.sights = sights;
        this.inflater = LayoutInflater.from(context);
        this.activity = context;
    }

    @Override
    public SightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sight_layout, parent, false);
        return new SightHolder(view);
    }

    @Override
    public void onBindViewHolder(SightsAdapter.SightHolder holder, int position) {
        Sight sight = sights.get(position);
        // Добавляем контент в sight_layout
        holder.title.setText(sight.getTitle());
        holder.name.setText(sight.getTitle());
        holder.description.setText(sight.getDescription());
        // Чтобы после нажатия "Показать на карте" и перехода в MapFragment
        // карта перешла к достопримечательности
        holder.cameraPosition = new CameraPosition(
                new Point(sight.getLatitude(), sight.getLongitude()),
                17f,
                0f,
                0f
        );
        holder.sight = sight;
        // Проверяем, что место не встроенное
        if (sight.getId() == -1) {
            // Если оно встроенное, то ликвидируем возможность
            // изменить или удалить его
            holder.deleteSight.setVisibility(View.GONE);
            holder.editSight.setVisibility(View.GONE);
        }
        holder.addListeners();
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }

    class SightHolder extends RecyclerView.ViewHolder{

        private TextView title, name, description;
        private LinearLayoutCompat titleLayout;
        private ConstraintLayout content;
        private AppCompatButton showOnMapBtn;
        private AppCompatImageButton deleteSight, editSight;
        private ImageView triangle;
        private CameraPosition cameraPosition;
        private Sight sight;

        public SightHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.sightTitleText);
            this.name = itemView.findViewById(R.id.sightName);
            this.description = itemView.findViewById(R.id.sightDescription);
            this.content = itemView.findViewById(R.id.sightContentLayout);
            this.showOnMapBtn = itemView.findViewById(R.id.gotoSightCoordinatesBtn);
            this.titleLayout = itemView.findViewById(R.id.sightTitleLayout);
            this.deleteSight = itemView.findViewById(R.id.deleteSightBtn);
            this.editSight = itemView.findViewById(R.id.editSightBtn);
            this.triangle = itemView.findViewById(R.id.sightTriangle);
        }

        private void addListeners(){
            // Пользователь нажал на заголовок
            this.titleLayout.setOnClickListener(view -> {
                CustomAnimations.toggleVisibility(content, triangle);
            });
            // Пользователь нажал на кнопку удаления
            this.deleteSight.setOnClickListener(view -> {
                ViewModelProvider provider = new ViewModelProvider(activity);
                // На всякий случай еще раз проверяем, что это пользовательское место
                if (sight.getId() != -1) {
                    provider.get(SightsViewModel.class).delete(sight);
                }
            });
            // Пользователь нажал на кнопку редактирования
            this.editSight.setOnClickListener(view -> {
                // Идём в EditSightActivity (активность редактирования)
                Intent intent = new Intent(activity, EditSightActivity.class);
                intent.putExtra(EditSightActivity.SIGHT, sight.toJson());
                activity.startActivity(intent);
            });
            // Пользователь нажал на кнопку "Показать на карте"
            this.showOnMapBtn.setOnClickListener(view -> {
                // Устанавливаем позицию камеры на ту, что пренадлежит нашему Sight
                ViewModelProvider provider = new ViewModelProvider(activity);
                provider.get(CurrentPositionViewModel.class).setValue(cameraPosition);

                // Переходим к MapFragment
                NavController controller = Navigation.findNavController(
                        activity, R.id.nav_host_fragment_content_main
                );
                controller.navigate(R.id.nav_map);
            });
        }
    }
}
