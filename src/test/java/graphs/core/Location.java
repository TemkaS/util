package graphs.core;



public enum Location {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;

    private Type type;


    public Type getType() {
        return type;
    }


    public static enum Type {
        X,
        Y;

        static {
            LEFT.type   = Y;
            TOP.type    = X;
            RIGHT.type  = Y;
            BOTTOM.type = X;

            X.defaultLocation = BOTTOM;
            Y.defaultLocation = LEFT;
        }

        private Location defaultLocation;

        public Location getDefaultLocation() {
            return defaultLocation;
        }

    }

}
