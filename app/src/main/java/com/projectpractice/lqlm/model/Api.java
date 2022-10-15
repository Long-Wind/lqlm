package com.projectpractice.lqlm.model;

import com.projectpractice.lqlm.model.entity.Category;
import com.projectpractice.lqlm.model.entity.HomePagerContent;
import com.projectpractice.lqlm.model.entity.SearchRecommend;
import com.projectpractice.lqlm.model.entity.SearchResult;
import com.projectpractice.lqlm.model.entity.SelectedContent;
import com.projectpractice.lqlm.model.entity.SelectedPageCategory;
import com.projectpractice.lqlm.model.entity.TicketParams;
import com.projectpractice.lqlm.model.entity.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("discovery/categories")
    Call<Category> getCategories();

    @GET("discovery/{materialId}/{page}")
    Call<HomePagerContent> getHomePagerContent(@Path("materialId") int materialId, @Path("page") int page);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET("recommend/categoryId")
    Call<SelectedContent> getSelectedPageContent(@Query("categoryId") int categoryId);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page, @Query("keyword") String keyword);
}
