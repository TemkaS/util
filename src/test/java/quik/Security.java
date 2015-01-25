package quik;




public class Security {
    private final int emitent;
    private final String code;
    private final String title;
    private final int market = 1;


    public Security(int emitent, String code, String title) {
        this.emitent = emitent;
        this.code = code;
        this.title = title;
    }


    public int getEmitent() {
        return emitent;
    }


    public String getCode() {
        return code;
    }


    public String getTitle() {
        return title;
    }


    public int getMarket() {
        return market;
    }


    @Override
    public String toString() {
        return "[" + emitent + "; " + code + "; " + title + "]";
    }

}
