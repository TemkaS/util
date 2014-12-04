package graphs.base;




public class NumberBase implements Base {
    protected final String name;
    protected final double[] data;


    public NumberBase(String name, double[] data) {
        if (name == null)
            throw new IllegalArgumentException("Name can't be null");

        if (data == null)
            throw new IllegalArgumentException("Data can't be null");

        this.name = name;
        this.data = data;
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public int size() {
        return data.length;
    }


    public double get(int index) {
        return data[index];
    }


}
