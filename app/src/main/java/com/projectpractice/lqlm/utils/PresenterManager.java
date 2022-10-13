package com.projectpractice.lqlm.utils;

import com.projectpractice.lqlm.presenter.ICategoryPagerPresenter;
import com.projectpractice.lqlm.presenter.IHomePresenter;
import com.projectpractice.lqlm.presenter.ISelectedPagePresenter;
import com.projectpractice.lqlm.presenter.ITicketPresenter;
import com.projectpractice.lqlm.presenter.impl.CategoryPagerPresenterImpl;
import com.projectpractice.lqlm.presenter.impl.HomePresenterImpl;
import com.projectpractice.lqlm.presenter.impl.SelectedPagePresenterImpl;
import com.projectpractice.lqlm.presenter.impl.TicketPresenterImpl;

public class PresenterManager {

    private static final PresenterManager ourInstance = new PresenterManager();
    private final IHomePresenter homePresenter;
    private final ICategoryPagerPresenter categoryPagerPresenter;
    private final ITicketPresenter ticketPresenter;
    private final ISelectedPagePresenter selectedPagePresenter;

    public static PresenterManager getInstance(){
        return ourInstance;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return categoryPagerPresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return ticketPresenter;
    }

    public ISelectedPagePresenter getSelectedPagePresenter() {
        return selectedPagePresenter;
    }

    private PresenterManager() {
        homePresenter = new HomePresenterImpl();
        categoryPagerPresenter = new CategoryPagerPresenterImpl();
        ticketPresenter = new TicketPresenterImpl();
        selectedPagePresenter = new SelectedPagePresenterImpl();
    }
}
