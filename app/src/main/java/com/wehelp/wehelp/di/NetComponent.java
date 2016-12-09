package com.wehelp.wehelp.di;

import com.wehelp.wehelp.AbandonEventActivity;
import com.wehelp.wehelp.CreateEventActivity;
import com.wehelp.wehelp.EventDetailActivity;
import com.wehelp.wehelp.FirstScreenActivity;
import com.wehelp.wehelp.HelpEventActivity;
import com.wehelp.wehelp.LoginActivity;
import com.wehelp.wehelp.MainActivity;
import com.wehelp.wehelp.MyEventsActivity;
import com.wehelp.wehelp.ParticipateEventActivity;
import com.wehelp.wehelp.TabbedActivity;
import com.wehelp.wehelp.controllers.UserController;
import com.wehelp.wehelp.tabs.FragmentMap;
import com.wehelp.wehelp.tabs.FragmentTimeline;
import com.wehelp.wehelp.tabs.tabs_register.FragmentOngRegister;
import com.wehelp.wehelp.tabs.tabs_register.FragmentPersonRegister;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(UserController controller);
    void inject(FragmentMap fragment);
    void inject(FragmentTimeline fragment);
    void inject(FragmentPersonRegister fragment);
    void inject(FragmentOngRegister fragment);
    void inject(CreateEventActivity activity);
    void inject(TabbedActivity activity);
    void inject(FirstScreenActivity activity);
    void inject(ParticipateEventActivity activity);
    void inject(HelpEventActivity activity);
    void inject(MyEventsActivity activity);
    void inject(AbandonEventActivity activity);
}