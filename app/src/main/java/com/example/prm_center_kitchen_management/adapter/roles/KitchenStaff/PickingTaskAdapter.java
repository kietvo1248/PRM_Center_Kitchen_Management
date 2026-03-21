package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.PickingTask;
import java.util.List;
import java.util.Locale;

public class PickingTaskAdapter extends RecyclerView.Adapter<PickingTaskAdapter.ViewHolder> {

    private List<PickingTask> taskList;
    private final OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onStartPicking(PickingTask task);
    }

    public PickingTaskAdapter(List<PickingTask> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picking_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickingTask task = taskList.get(position);
        holder.tvOrderId.setText(buildOrderTitle(task));

        String st = task.getStatus() != null ? task.getStatus() : "";
        holder.tvStatus.setText(st.toUpperCase(Locale.ROOT));

        int itemCount = task.getItems() != null ? task.getItems().size() : 0;
        holder.tvTaskDetails.setText(String.format(Locale.getDefault(),
                "Dòng sản phẩm: %d", itemCount));

        applyStatusBadge(holder.tvStatus, st);

        boolean finished = isTerminalCompleted(st);
        holder.btnStartPicking.setVisibility(finished ? View.GONE : View.VISIBLE);

        if ("picking".equalsIgnoreCase(st) || "delivering".equalsIgnoreCase(st)) {
            holder.btnStartPicking.setText("TIẾP TỤC SOẠN");
        } else {
            holder.btnStartPicking.setText("BẮT ĐẦU SOẠN");
        }

        holder.btnStartPicking.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStartPicking(task);
            }
        });
    }

    /**
     * Badge: draft = xám; pending / preparing / approved / picking = vàng;
     * completed / in_transit / delivered = xanh.
     */
    private static void applyStatusBadge(TextView tvStatus, String raw) {
        String s = raw != null ? raw.toLowerCase(Locale.ROOT) : "";
        switch (s) {
            case "draft":
            case "cancelled":
                tvStatus.setBackgroundResource(R.drawable.bg_status_draft);
                break;
            case "pending":
            case "preparing":
            case "approved":
            case "picking":
            case "delivering":
                tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                break;
            case "completed":
            case "in_transit":
            case "delivered":
                tvStatus.setBackgroundResource(R.drawable.bg_status_completed);
                break;
            default:
                tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                break;
        }
    }

    private static boolean isTerminalCompleted(String raw) {
        if (raw == null) return false;
        String s = raw.toLowerCase(Locale.ROOT);
        return "completed".equals(s) || "delivered".equals(s);
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public void updateData(List<PickingTask> newList) {
        this.taskList = newList;
        notifyDataSetChanged();
    }

    /** orderCode (ưu tiên) → orderId → id */
    private static String buildOrderTitle(PickingTask task) {
        if (task.getOrderCode() != null && !task.getOrderCode().isEmpty()) {
            return task.getOrderCode();
        }
        if (task.getOrderId() != null && !task.getOrderId().isEmpty()) {
            String oid = task.getOrderId();
            return oid.length() > 14 ? "Đơn #" + oid.substring(0, 8) + "…" : "Đơn #" + oid;
        }
        if (task.getId() != null && !task.getId().isEmpty()) {
            String id = task.getId();
            return id.length() > 14 ? "Đơn #" + id.substring(0, 8) + "…" : "Đơn #" + id;
        }
        return "Đơn";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvStatus, tvTaskDetails;
        Button btnStartPicking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTaskDetails = itemView.findViewById(R.id.tvTaskDetails);
            btnStartPicking = itemView.findViewById(R.id.btnStartPicking);
        }
    }
}
