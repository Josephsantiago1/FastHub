package com.fastaccess.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.fastaccess.data.dao.TimelineModel;
import com.fastaccess.data.dao.model.Comment;
import com.fastaccess.data.dao.model.Login;
import com.fastaccess.ui.adapter.callback.OnToggleView;
import com.fastaccess.ui.adapter.callback.ReactionsCallback;
import com.fastaccess.ui.adapter.viewholder.IssueDetailsViewHolder;
import com.fastaccess.ui.adapter.viewholder.IssueTimelineViewHolder;
import com.fastaccess.ui.adapter.viewholder.TimelineCommentsViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 13 Dec 2016, 1:44 AM
 */

public class IssuePullsTimelineAdapter extends BaseRecyclerAdapter<TimelineModel, BaseViewHolder,
        BaseViewHolder.OnItemClickListener<TimelineModel>> {

    private final OnToggleView onToggleView;
    private final String login;
    private final boolean showEmojies;
    private final ReactionsCallback reactionsCallback;

    public IssuePullsTimelineAdapter(@NonNull List<TimelineModel> data, OnToggleView onToggleView, boolean showEmojies,
                                     ReactionsCallback reactionsCallback) {
        super(data);
        this.onToggleView = onToggleView;
        this.login = Login.getUser().getLogin();
        this.showEmojies = showEmojies;
        this.reactionsCallback = reactionsCallback;
    }

    @Override protected BaseViewHolder viewHolder(ViewGroup parent, int viewType) {
        if (viewType == TimelineModel.HEADER) {
            return IssueDetailsViewHolder.newInstance(parent, this);
        } else if (viewType == TimelineModel.EVENT) {
            return IssueTimelineViewHolder.newInstance(parent, this);
        }
        return TimelineCommentsViewHolder.newInstance(parent, this, login, onToggleView, showEmojies, reactionsCallback);
    }

    @Override protected void onBindView(BaseViewHolder holder, int position) {
        TimelineModel model = getItem(position);
        if (model.getType() == TimelineModel.HEADER) {
            if (model.getIssue() != null) {
                ((IssueDetailsViewHolder) holder).bind(model);
            } else if (model.getPullRequest() != null) {
                ((IssueDetailsViewHolder) holder).bind(model);
            }
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else if (model.getType() == TimelineModel.EVENT) {
            ((IssueTimelineViewHolder) holder).bind(model);
        } else {
            ((TimelineCommentsViewHolder) holder).bind(model);
        }
    }

    @Override public int getItemViewType(int position) {
        return getData().get(position).getType();
    }

    @Override public void onViewRecycled(BaseViewHolder holder) {
        if (holder instanceof TimelineCommentsViewHolder) {
            Comment comment = getItem(holder.getAdapterPosition()).getComment();
            if (comment == null || comment.getBodyHtml() == null) {
                super.onViewRecycled(holder);
            }
        } else {
            super.onViewRecycled(holder);
        }
    }

}

