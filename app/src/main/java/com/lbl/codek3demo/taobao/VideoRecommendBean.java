package com.lbl.codek3demo.taobao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yons on 16/5/24.
 */
public class VideoRecommendBean {

    /**
     * track_info : {"biz":"recommend","info":"detail_rec#detail_itm#5f6904a0-217f-11e6-a480-afae987bd0ea"}
     * pos : 7
     * videos_info : [{"video_id":569,"imgURL":"http://v.xiaohongchun.com/CE0DB6A3921B4BB7-cover1.jpg","like":0,"play":0,"title":"#悦诗风吟#悦诗风吟..."},{"video_id":571,"imgURL":"http://v.xiaohongchun.com/E1D9679B58932D36-cover1.jpg","like":0,"play":0,"title":"特别喜欢#爱丽小屋#..."}]
     */

    private String track_info;
    private int pos;
    /**
     * video_id : 569
     * imgURL : http://v.xiaohongchun.com/CE0DB6A3921B4BB7-cover1.jpg
     * like : 0
     * play : 0
     * title : #悦诗风吟#悦诗风吟...
     */

    private List<VideosInfoEntity> videos_info;

    public String getTrack_info() {
        return track_info;
    }

    public void setTrack_info(String track_info) {
        this.track_info = track_info;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public List<VideosInfoEntity> getVideos_info() {
        return videos_info;
    }

    public void setVideos_info(List<VideosInfoEntity> videos_info) {
        this.videos_info = videos_info;
    }

    public static class VideosInfoEntity implements Parcelable {
        private int video_id;
        private String imgURL;
        private int like;
        private int play;
        private String title;
        private String duration;
        private int userid;
        private String avatar;
        private Long date_add;
        private String username;
        private String jump_url;
        public String dcrp="";

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Long getDate_add() {
            return date_add;
        }

        public void setDate_add(Long date_add) {
            this.date_add = date_add;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public int getPlay() {
            return play;
        }

        public void setPlay(int play) {
            this.play = play;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.video_id);
            dest.writeString(this.imgURL);
            dest.writeInt(this.like);
            dest.writeInt(this.play);
            dest.writeString(this.title);
            dest.writeString(this.duration);
            dest.writeInt(this.userid);
            dest.writeString(this.avatar);
            dest.writeValue(this.date_add);
            dest.writeString(this.username);
        }

        public VideosInfoEntity() {
        }

        protected VideosInfoEntity(Parcel in) {
            this.video_id = in.readInt();
            this.imgURL = in.readString();
            this.like = in.readInt();
            this.play = in.readInt();
            this.title = in.readString();
            this.duration = in.readString();
            this.userid = in.readInt();
            this.avatar = in.readString();
            this.date_add = (Long) in.readValue(Long.class.getClassLoader());
            this.username = in.readString();
        }

        public static final Creator<VideosInfoEntity> CREATOR = new Creator<VideosInfoEntity>() {
            @Override
            public VideosInfoEntity createFromParcel(Parcel source) {
                return new VideosInfoEntity(source);
            }

            @Override
            public VideosInfoEntity[] newArray(int size) {
                return new VideosInfoEntity[size];
            }
        };

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }
    }
}
