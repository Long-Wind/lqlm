package com.projectpractice.lqlm.presenter;

import android.content.Context;

import com.projectpractice.lqlm.base.IBasePresenter;
import com.projectpractice.lqlm.view.ITicketCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketCallback> {
    void getTicket(String title, String url, String cover);
}
