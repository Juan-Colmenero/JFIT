package e.juancolmenero.jfit.Models;

import java.util.ArrayList;

public class PostUserModel {
    private String name, status, picture, date;
    private int post_id, user_id;

    private ArrayList<PostBodyTypeModel> arrayList;
    //state of the item
    private boolean expanded;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PostBodyTypeModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<PostBodyTypeModel> arrayList) {
        this.arrayList = arrayList;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
