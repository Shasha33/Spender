package com.project.spender.roller;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ChecksRollerModule {

    @Provides
    @Singleton
    @NonNull
    public ChecksRoller provideChecksRoller(Context context) {
        return new ChecksRoller(context);
    }

}
