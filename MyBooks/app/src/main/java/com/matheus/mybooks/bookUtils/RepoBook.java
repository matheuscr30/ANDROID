package com.matheus.mybooks.bookUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RepoBook{
    @SerializedName("totalItems")
    @Expose
    private Integer totalItems;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public class Item {

        @SerializedName("volumeInfo")
        @Expose
        private VolumeInfo volumeInfo;
        @SerializedName("id")
        @Expose
        private String id;

        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }

        public void setVolumeInfo(VolumeInfo volumeInfo) {
            this.volumeInfo = volumeInfo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public class VolumeInfo {

            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("subtitle")
            @Expose
            private String subtitle;
            @SerializedName("authors")
            @Expose
            private List<String> authors = null;
            @SerializedName("publishedDate")
            @Expose
            private String publishedDate;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("pageCount")
            @Expose
            private Integer pageCount;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public List<String> getAuthors() {
                return authors;
            }

            public void setAuthors(List<String> authors) {
                this.authors = authors;
            }

            public String getPublishedDate() {
                return publishedDate;
            }

            public void setPublishedDate(String publishedDate) {
                this.publishedDate = publishedDate;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Integer getPageCount() {
                return pageCount;
            }

            public void setPageCount(Integer pageCount) {
                this.pageCount = pageCount;
            }

        }
    }
}

