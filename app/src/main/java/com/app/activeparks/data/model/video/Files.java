package com.app.activeparks.data.model.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Files {

        @SerializedName("low")
        @Expose
        private String low;
        @SerializedName("high")
        @Expose
        private String high;

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

}
