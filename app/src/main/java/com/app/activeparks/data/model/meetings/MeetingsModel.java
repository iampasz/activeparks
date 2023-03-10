package com.app.activeparks.data.model.meetings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeetingsModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("items")
    @Expose
    private List<MeetingItem> items;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<MeetingItem> getItems() {
        return items;
    }

    public void setItems(List<MeetingItem> items) {
        this.items = items;
    }

    public class MeetingItem {

        @SerializedName("meetingID")
        @Expose
        private String meetingID;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("participants")
        @Expose
        private String participants;
        @SerializedName("startTime")
        @Expose
        private String startTime;
        @SerializedName("endTime")
        @Expose
        private String endTime;
        @SerializedName("length")
        @Expose
        private String length;
        @SerializedName("url")
        @Expose
        private String url;

        public String getMeetingID() {
            return meetingID;
        }

        public void setMeetingID(String meetingID) {
            this.meetingID = meetingID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}