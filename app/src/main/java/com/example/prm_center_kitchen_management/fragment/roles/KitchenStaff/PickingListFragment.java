package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff.PickingTaskAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;
import com.example.prm_center_kitchen_management.model.response.PickingTask;
import com.example.prm_center_kitchen_management.utils.ApiUiHelper;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Danh sách soạn hàng — không tab filter. GET /warehouse/picking-tasks (phân trang + sort).
 */
public class PickingListFragment extends Fragment implements PickingTaskAdapter.OnTaskClickListener {

    private static final String TAG = "PickingList";

    private RecyclerView rvPickingTasks;
    private PickingTaskAdapter adapter;
    private final List<PickingTask> taskList = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picking_list, container, false);
        initViews(view);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        fetchPickingTasks();

        swipeRefresh.setOnRefreshListener(this::fetchPickingTasks);

        return view;
    }

    private void initViews(View view) {
        rvPickingTasks = view.findViewById(R.id.rvPickingTasks);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        rvPickingTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PickingTaskAdapter(taskList, this);
        rvPickingTasks.setAdapter(adapter);
    }

    private void fetchPickingTasks() {
        fetchPickingTasksInternal(true, false);
    }

    private void fetchPickingTasksInternal(boolean allowRetryOn400, boolean forceOmitStatus) {
        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        String statusQuery = forceOmitStatus ? null : "approved";

        apiService.getPickingTasks(1, 50, statusQuery, null, null, "status", "DESC").enqueue(new Callback<ApiResponse<PaginatedResponse<PickingTask>>>() {
            @Override
            public void onResponse(
                    @NonNull Call<ApiResponse<PaginatedResponse<PickingTask>>> call,
                    @NonNull Response<ApiResponse<PaginatedResponse<PickingTask>>> response
            ) {
                if (response.code() == 400 && allowRetryOn400 && statusQuery != null) {
                    Log.w(TAG, "HTTP 400 — thử lại không gửi status");
                    fetchPickingTasksInternal(false, true);
                    return;
                }

                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                if (!response.isSuccessful() || response.body() == null) {
                    if (response.code() == 401 || response.code() == 403) {
                        ApiUiHelper.toastHttpError(requireContext(), response);
                    } else {
                        Toast.makeText(getContext(), "Không tải được danh sách (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                PaginatedResponse<PickingTask> page = response.body().getData();
                List<PickingTask> raw = page != null ? page.getItems() : null;
                taskList.clear();
                if (raw != null) {
                    taskList.addAll(raw);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PaginatedResponse<PickingTask>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "fetch failed", t);
                Toast.makeText(getContext(), "Lỗi: " + (t.getMessage() != null ? t.getMessage() : ""), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStartPicking(PickingTask task) {
        String detailId = task.getId();
        if (detailId == null || detailId.isEmpty()) {
            detailId = task.getOrderId();
        }
        if (detailId == null || detailId.isEmpty()) {
            Toast.makeText(getContext(), "Thiếu mã tác vụ", Toast.LENGTH_SHORT).show();
            return;
        }
        PickingDetailFragment detailFragment = PickingDetailFragment.newInstance(detailId);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
