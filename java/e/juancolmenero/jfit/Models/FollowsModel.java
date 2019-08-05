package e.juancolmenero.jfit.Models;

public class FollowsModel {
    private String name,
            userName,
            request,
            isFollowing;
    private int follower_id;

    public int getFollowerId(){
        return follower_id;
    }

    public void setFollower_id(int follower_id){
        this.follower_id = follower_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsFollowing(){
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing){
        this.isFollowing = isFollowing;
    }

    public String getRequestType(){
        return request;
    }

    public void setRequestType(String request) {
        this.request = request;
    }

}
