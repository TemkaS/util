package graphs.core;




public class Rectangle implements Cloneable {
    private int top;
    private int right;
    private int bottom;
    private int left;


    public Rectangle() {
        this(0, 0, 0, 0);
    }


    public Rectangle(int left, int top, int right, int bottom) {
        this.left   = left;
        this.top    = top;
        this.right  = right;
        this.bottom = bottom;
    }


    public int getTop() {
        return top;
    }


    public void setTop(int top) {
        this.top = top;
    }


    public int addTop(int delta) {
        top+= delta;
        return top;
    }


    public int getRight() {
        return right;
    }


    public void setRight(int right) {
        this.right = right;
    }


    public int addRight(int delta) {
        right+= delta;
        return right;
    }


    public int getBottom() {
        return bottom;
    }


    public void setBottom(int bottom) {
        this.bottom = bottom;
    }


    public int addBottom(int delta) {
        bottom+= delta;
        return bottom;
    }


    public int getLeft() {
        return left;
    }


    public void setLeft(int left) {
        this.left = left;
    }


    public int addLeft(int delta) {
        left+= delta;
        return left;
    }


    @Override
    protected Rectangle clone() {
        try {
            return (Rectangle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
