package com.project.spender.roller;

import com.project.spender.activities.HistoryActivity;
import com.project.spender.activities.LoginActivity;
import com.project.spender.activities.MainActivity;
import com.project.spender.activities.ScanActivity;
import com.project.spender.adapters.ItemAdapter;
import com.project.spender.adapters.ListAdapter;
import com.project.spender.controllers.CheckListHolder;
import com.project.spender.controllers.CheckShowHelper;
import com.project.spender.controllers.TagChoiceHelper;
import com.project.spender.controllers.TagListHelper;
import com.project.spender.controllers.TagStateHolder;
import com.project.spender.fragments.ChartFragment;
import com.project.spender.fragments.PieChartFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ChecksRollerModule.class})
@Singleton
public interface AppComponent {
    void inject(HistoryActivity activity);
    void inject(LoginActivity activity);
    void inject(ChartFragment fragment);
    void inject(PieChartFragment fragment);
    void inject(CheckShowHelper helper);
    void inject(CheckListHolder holder);
    void inject(TagListHelper helper);
    void inject(TagChoiceHelper helper);
    void inject(MainActivity activity);
    void inject(ScanActivity activity);
    void inject(ItemAdapter adapter);
    void inject(TagStateHolder holder);
    void inject(ListAdapter adapter);
}
