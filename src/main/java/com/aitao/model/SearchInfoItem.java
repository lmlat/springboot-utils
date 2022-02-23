package com.aitao.model;

public class SearchInfoItem {
        private String cover;//封面
        private String duration;//学习时长
        private String title;//标题
        private Long views;//浏览量
        private String uploadTime;//上传时间
        private String up;//up主
        private String bVId;//视频播放ID
        private String accessUrl;//视频访问URL
        private String authVoucher;//凭证
        private String createTime;//创建时间

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getViews() {
            return views;
        }

        public void setViews(Long views) {
            this.views = views;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getbVId() {
            return bVId;
        }

        public void setbVId(String bVId) {
            this.bVId = bVId;
        }

        public String getAccessUrl() {
            return accessUrl;
        }

        public void setAccessUrl(String accessUrl) {
            this.accessUrl = accessUrl;
        }

        public String getAuthVoucher() {
            return authVoucher;
        }

        public void setAuthVoucher(String authVoucher) {
            this.authVoucher = authVoucher;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }