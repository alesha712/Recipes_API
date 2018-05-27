package com.hqs.alx.recipes;

/**
 * Created by Alex on 01/11/2017.
 */

public class RecipeList {

    private String recipeHeader;
    private String recipeURL;

    private String publisherName;
    private Double rank;
    private String imageURL;
    private String publisherSite;

    public RecipeList(String recipeHeader, String recipeURL, String publisherName, Double socialRank, String imageUrl, String publisherSite) {
        this.recipeHeader = recipeHeader;
        this.recipeURL = recipeURL;
        this.publisherName = publisherName;
        this.rank = socialRank;
        this.imageURL = imageUrl;
        this.publisherSite = publisherSite;
    }

    public String getRecipeHeader() {
        return recipeHeader;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public String getPublisherName() {
        return publisherName;
    }


    public Double getRank() {
        return rank;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPublisherSite() {
        return publisherSite;
    }
}
