package util_old;


import java.util.*;


public class CsvFile {
    public static class UnexpectedEndOfCell extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public static class VariableColumnCount extends Exception {
        private static final long serialVersionUID = 1L;
    }


    private String sdata = null;
    private int length   = 0;
    private int index    = 0;
    private boolean insert = false;
    private char colsep  = ';';

    private List<List<String>> data = new ArrayList<List<String>>();



    public CsvFile() {
    }


    public boolean parse(String sdata, char colsep) throws Exception {
        try {
            this.sdata  = sdata;
            this.length = sdata.length();
            this.index  = 0;
            this.insert = true;
            this.colsep = colsep;

            while(this.index < this.length) {
                if(sdata.charAt(this.index) == '"') {
                    this.readQuoted();
                }else {
                    this.readNormal();
                }
            }

            int col = -1;
            for(List<String> c : this.data) {
                if(col < 0) {
                    col = c.size();
                }else
                if(col != c.size()){
                    throw new VariableColumnCount();
                }
            }

            return true;

        } finally {
            this.sdata = null;
        }

    }




    public List<List<String>> getData(){
        return this.data;
    }


    public List<String> getRow(int index){
        return index >= 0 && index < this.data.size() ? this.data.get(index) : null;
    }


    public int numRows() {
        return this.data.size();
    }


    public int numCols() {
        return this.data.size() > 0 ? this.data.get(0).size() : 0;
    }



    private void readQuoted() throws Exception {
        int start = this.index++,
            quotes = 1;

        while(this.index < this.length) {
            if(this.sdata.charAt(this.index) == '"') {
                quotes++;

            }else
            if((quotes & 1) == 0) {
                if(this.checkEnd(start, true))
                    return;

                throw new UnexpectedEndOfCell();
            }
            this.index++;
        }

        if(this.checkEnd(start, true))
            return;

        throw new UnexpectedEndOfCell();

    }


    private void readNormal() throws Exception {
        int start = this.index;

        while(this.index < this.length) {
            if(this.checkEnd(start, false))
                return;

            this.index++;
        }

        this.checkEnd(start, false);

    }


    private boolean checkEnd(int start, boolean quoted) {
        int delta = 0,
            endof = 0;

        if(this.index >= this.length) {
            endof = 2;
            delta = 0;

        } else {
            char c = this.sdata.charAt(this.index);

            if(c == this.colsep) {
                endof = 1;
                delta = 1;

            }else
            if(c == '\n') {
                endof = 2;
                delta = 1;

            }else
            if(c == '\r') {
                endof = 2;
                delta = 1;

                if(this.index + 1 < this.length) {
                    c = this.sdata.charAt(this.index + 1);
                    if(c == '\n') {
                        endof = 2;
                        delta = 2;
                    }
                }

            }
        }

        if(endof == 0)
            return false;

        String cell = quoted ? this.sdata.substring(start + 1, this.index - 1).replace("\"\"", "\"")
                             : this.sdata.substring(start,     this.index);

    if(this.insert || this.data.size() == 0)
          this.data.add(new ArrayList<String>());

        this.data.get(this.data.size() - 1).add(cell);
        this.insert = endof == 2;

        this.index+= delta;
        return true;
    }




    public static String escape(String value){
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

}

