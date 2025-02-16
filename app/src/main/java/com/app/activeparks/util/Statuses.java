package com.app.activeparks.util;

import com.app.activeparks.ui.userProfile.model.VideoState;
import com.technodreams.activeparks.R;

public class Statuses {

    public String title(String id) {
        switch (id){
            case "f7b77851-1d35-4ab1-9fcd-ef6cd2b88ace": return "Деактивований";
            case "def3497e-3498-4a17-a514-e99ec83676b4": return "На модерації";
            case "66e2ec16-03a1-4367-834a-d6b87ee709bd": return "Опублікований";
            case "73f891c0-a79f-4afc-bd79-d529a5eb5774": return "Чернетка";
            default:
                return "Опублікований";
        }
    }

    public static VideoState getStatus(String id) {
        switch (id){
            case "f7b77851-1d35-4ab1-9fcd-ef6cd2b88ace": return VideoState.DEACTIVATE;
            case "def3497e-3498-4a17-a514-e99ec83676b4": return VideoState.MODERATION;
            case "73f891c0-a79f-4afc-bd79-d529a5eb5774": return VideoState.DRAFT;
            default:
                return VideoState.PUBLISHED;
        }
    }

    public int color(String id){
        switch (id){
            case "f7b77851-1d35-4ab1-9fcd-ef6cd2b88ace": return R.drawable.button_black;
            case "def3497e-3498-4a17-a514-e99ec83676b4": return R.drawable.button_orange;
            case "66e2ec16-03a1-4367-834a-d6b87ee709bd": return R.drawable.button_color;
            case "73f891c0-a79f-4afc-bd79-d529a5eb5774": return R.drawable.button_pink;
            default:
                return R.drawable.button_color;
        }
    }

    public int textColor(String id){
        switch (id){
            case "f7b77851-1d35-4ab1-9fcd-ef6cd2b88ace": return R.color.text_color;
            case "def3497e-3498-4a17-a514-e99ec83676b4": return R.color.white;
            case "66e2ec16-03a1-4367-834a-d6b87ee709bd": return R.color.white;
            case "73f891c0-a79f-4afc-bd79-d529a5eb5774": return R.color.white;
            default:
                return R.color.white;
        }
    }
}
