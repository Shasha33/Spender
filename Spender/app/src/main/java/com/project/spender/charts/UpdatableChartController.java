package com.project.spender.charts;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.project.spender.data.entities.TagWithSum;
import com.project.spender.data.entities.TagWithSumAndDate;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Abstract class for view controller for updatable charts
 */
public abstract class UpdatableChartController <T> {
    protected final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss", Locale.ROOT);
    protected LiveData<T> dataSource;
    protected Observer<T> observer = this::setData;
    protected LifecycleOwner owner;
    protected Set<Long> whiteIdList;

    public void setDataSource(LiveData<T> data) {
        if (dataSource != null) {
            dataSource.removeObserver(observer);
        }
        dataSource = data;
        dataSource.observe(owner, observer);
    }

    protected abstract void setData(T chartData);

    public void setWhiteIdList(Set<Long> whiteIdList) {
        this.whiteIdList = whiteIdList;
    }
}
