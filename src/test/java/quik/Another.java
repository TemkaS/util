package quik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;





public class Another {

    public static void main(String[] args) {
        Object[][] data1 = {
                {0, "TRMK"},
                {1, "URKA"},
                {2, "KBTK"},
                {3, "NVTK"},
                {4, "KBSB"},
                {5, "RASP"},
                {6, "TATN"},
                {7, "MTSS"},
                {8, "LKOH"},
                {9, "SBER"},
                {10, "AFLT"},
                {11, "SBERP"},
                {12, "SNGS"},
                {13, "SNGSP"},
                {14, "AFKS"},
                {15, "MTLR"},
                {16, "BANE"},
                {17, "GMKN"},
                {18, "VTBR"},
                {19, "BANEP"},
                {20, "DASB"},
                {21, "ALRS"},
                {22, "CHMF"},
                {23, "ROSN"},
                {24, "SIBN"},
                {25, "HYDR"},
                {26, "MGNT"},
                {27, "MTLRP"},
                {28, "MOEX"},
                {29, "RTKM"},
                {30, "LSRG"},
                {31, "SAREP"},
                {32, "GAZP"},
                {33, "MAGN"},
                {34, "MVID"},
                {35, "RSTI"},
                {36, "NLMK"},
                {37, "FEES"},
                {38, "BLNG"},
                {39, "ARMD"},
                {40, "OPIN"},
                {41, "MRKK"},
                {42, "AMEZ"},
                {43, "TRNFP"},
                {44, "AVAZP"},
                {45, "PRIN"},
                {46, "GRAZ"},
                {47, "CHZN"},
                {48, "TGKBP"},
                {49, "BSPB"},
                {50, "RNAV"},
                {51, "IRAO"},
                {52, "OGKB"},
                {53, "CLSBP"},
                {54, "KUBE"},
                {55, "TORS"},
                {56, "EONR"},
                {57, "TUCH"},
                {58, "MFON"},
                {59, "TORSP"},
                {60, "PLZL"},
                {61, "TATNP"},
                {62, "UTAR"},
                {63, "KRKOP"},
                {64, "PHOR"},
                {65, "ISKJ"},
                {66, "GUMM"},
                {67, "RUALR"},
                {68, "MSTT"},
                {69, "NKNC"},
                {70, "RTKMP"},
                {71, "AVAZ"},
                {72, "GTPR"},
                {73, "OTCP"},
                {74, "LNZLP"},
                {75, "MRKP"},
                {76, "POLY"},
                {77, "KAZT"},
                {78, "VSMO"},
                {79, "AKRN"},
                {80, "ARSA"},
                {81, "ELTZ"},
                {82, "NMTP"},
                {83, "MSRS"},
                {84, "DIXY"},
                {85, "SVAV"},
                {86, "KRSBP"},
                {87, "TGKA"},
                {88, "TGKD"},
                {89, "TGKDP"},
                {90, "RSTIP"},
                {91, "ROST"},
                {92, "YASH"},
                {93, "IRGZ"},
                {94, "MRKY"},
                {95, "RBCM"},
                {96, "MSNG"},
                {97, "SELG"},
                {98, "MAGE"},
                {99, "PRTK"}
        };

        Object[][] data2 = {
                {0, "KBTK"},
                {1, "TRMK"},
                {2, "KRSBP"},
                {3, "AMEZ"},
                {4, "KBSB"},
                {5, "VGSBP"},
                {6, "MAGE"},
                {7, "SARE"},
                {8, "YRSBP"},
                {9, "NKSH"},
                {10, "ELTZ"},
                {11, "GUMM"},
                {12, "KAZT"},
                {13, "OPIN"},
                {14, "YASH"},
                {15, "TORS"},
                {16, "REBR"},
                {17, "DASB"},
                {18, "RNAV"},
                {19, "TORSP"},
                {20, "ARMD"},
                {21, "KRKOP"},
                {22, "ARSA"},
                {23, "ZMZN"},
                {24, "MAGEP"},
                {25, "LNZLP"},
                {26, "OTCP"},
                {27, "BRZL"},
                {28, "ISKJ"},
                {29, "BLNG"},
                {30, "LSRG"},
                {31, "SAREP"},
                {32, "SAGO"},
                {33, "RUSP"},
                {34, "LVHK"},
                {35, "SELG"},
                {36, "OSMP"},
                {37, "URKA"},
                {38, "TUCH"},
                {39, "PRIM"},
                {40, "RASP"},
                {41, "UKUZ"},
                {42, "NKNC"},
                {43, "CHEP"},
                {44, "MRKY"},
                {45, "PRIN"},
                {46, "AVAZP"},
                {47, "NVNGP"},
                {48, "UTII"},
                {49, "CNTL"},
                {50, "YNDX"},
                {51, "OMZZP"},
                {52, "ALRS"},
                {53, "KROTP"},
                {54, "MSRS"},
                {55, "MTLRP"},
                {56, "MVID"},
                {57, "BSPB"},
                {58, "MTLR"},
                {59, "RUALR"},
                {60, "MSSB"},
                {61, "OFCB"},
                {62, "BANE"},
                {63, "PLSM"},
                {64, "GRAZ"},
                {65, "CHZN"},
                {66, "TGKBP"},
                {67, "RSTIP"},
                {68, "USBN"},
                {69, "MRKP"},
                {70, "GTPR"},
                {71, "TGKA"},
                {72, "VSMO"},
                {73, "RSTI"},
                {74, "PRTK"},
                {75, "CHMF"},
                {76, "KMAZ"},
                {77, "MGNT"},
                {78, "PLZL"},
                {79, "RTKM"},
                {80, "MRKK"},
                {81, "AFLT"},
                {82, "MGVM"},
                {83, "MRKZ"},
                {84, "LSNG"},
                {85, "PMSB"},
                {86, "GCHE"},
                {87, "MOEX"},
                {88, "ROST"},
                {89, "PMSBP"},
                {90, "NVTK"},
                {91, "TATN"},
                {92, "GTLC"},
                {93, "AVAZ"},
                {94, "MTSS"},
                {95, "NMTP"},
                {96, "BANEP"},
                {97, "MOTZ"},
                {98, "MAGN"},
                {99, "TGKD"}
        };


        Map<String, Integer> map1 = new HashMap<String, Integer>();
        for (Object[] d : data1)
            map1.put((String) d[1], (Integer) d[0]);

        Map<String, Integer> map2 = new HashMap<String, Integer>();
        for (Object[] d : data2)
            map2.put((String) d[1], (Integer) d[0]);

        Set<String> codes = new HashSet<String>();
        codes.addAll(map1.keySet());
        codes.addAll(map2.keySet());

        List<Item> list = new ArrayList<Item>();

        for (String code : codes) {
            Integer val1 = map1.get(code);
            Integer val2 = map2.get(code);

            Item item = new Item();
            item.code = code;
            item.weight = (val1 != null ? (100 - val1) : 0) + (val2 != null ? (100 - val2) : 0);

            list.add(item);
        }

        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item a, Item b) {
                return Double.compare(b.weight, a.weight);
            }
        });

        for (int i = 0; i < list.size(); i++) {
            Security sec = Securities.getByCode(list.get(i).code);
            System.out.println(sec.getCode() + " " + sec.getTitle());
        }

    }


    public static class Item {
        String  code;
        double weight;
    }


}
