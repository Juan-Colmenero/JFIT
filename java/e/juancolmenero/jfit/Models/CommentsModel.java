package e.juancolmenero.jfit.Models;

public class CommentsModel {
    private String name, commentMessage;
    private int comment_id, post_id, user_id;

    public int getUser_id(){return user_id;}

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getComment_id(){
        return comment_id;
    }

    public void setComment_id(int comment_id){
        this.comment_id = comment_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }
}
