package e.juancolmenero.jfit.Models;

public class PostBodyTypeModel {

    private String bodyPart;
    //state of the item
    private boolean expanded;

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

}
