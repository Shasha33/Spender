package com.project.spender.fragments;

import androidx.fragment.app.Fragment;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.data.CheckDao;

import java.util.Set;

public abstract class ChartFragment extends Fragment {

    protected String leftDate = "1999-01-25";
    protected String rightDate = "now";
    protected Set<Long> whiteIdList;
    protected CheckDao checkDao = ChecksRoller.getInstance().getAppDatabase().getCheckDao();

    public void setWhiteIdList(Set<Long> whiteIdList) {
        this.whiteIdList = whiteIdList;
    }

    public void setPeriod(String leftDate, String rightDate) {
        this.leftDate = leftDate;
        this.rightDate = rightDate;
    }

    public abstract void resetData();

}
