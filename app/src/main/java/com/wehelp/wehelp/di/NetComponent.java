package com.wehelp.wehelp.di;

import com.wehelp.wehelp.LoginActivity;
import com.wehelp.wehelp.MainActivity;
import com.wehelp.wehelp.controllers.UserController;
import com.wehelp.wehelp.tabs.FragmentMap;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(UserController controller);
    void inject(FragmentMap fragment);
    // void inject(MyService service);
}