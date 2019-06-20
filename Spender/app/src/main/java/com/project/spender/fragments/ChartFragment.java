package com.project.spender.fragments;

import androidx.fragment.app.Fragment;

import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;
import com.project.spender.data.CheckDao;

import java.util.Set;

import javax.inject.Inject;

public abstract class ChartFragment extends Fragment {

    @Inject protected ChecksRoller checksRoller;

    protected String leftDate = "1999-01-25";
    protected String rightDate = "now";
    protected Set<Long> whiteIdList;
    protected CheckDao checkDao;

    public ChartFragment() {
        super();
        App.getComponent().inject(this);
        checkDao = checksRoller.getAppDatabase().getCheckDao();
    }

    public void setWhiteIdList(Set<Long> whiteIdList) {
        this.whiteIdList = whiteIdList;
    }

    public void setPeriod(String leftDate, String rightDate) {
        this.leftDate = leftDate;
        this.rightDate = rightDate;
    }

    public abstract void resetData();

}
