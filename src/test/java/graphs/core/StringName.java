package graphs.core;





public class StringName implements Name {
    private final String name;


    public StringName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Parameter can't be null");
        this.name = name;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof StringName))
            return false;

        StringName oth = (StringName) obj;
        return name.equals(oth.name);
    }

}
