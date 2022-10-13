package com.projectpractice.lqlm.view;

import com.projectpractice.lqlm.base.IBaseCallback;
import com.projectpractice.lqlm.model.entity.TicketResult;

public interface ITicketCallback extends IBaseCallback {
    void onTicketLoad(String cover, TicketResult ticket);
}
