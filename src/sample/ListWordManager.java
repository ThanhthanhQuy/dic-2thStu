package sample;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;

public class ListWordManager {
   private   Map <String, String> List1= new HashMap<String, String>();
    String part1; String part2; FileReader fr;BufferedReader br; String i;
    public Map<String, String> getList1() {
        return List1;
    }
    public void getpart1() {


    }
    public void CreatListWord(String Name_File) throws Exception {
        fr = new FileReader(Name_File);
        br= new BufferedReader(fr);
        while ((i = br.readLine()) != null) {
          //  System.out.println(i);
            if(i.indexOf("<")>0) {

                part1 = i.substring(0, i.indexOf("<")/*tim vi tri*/);
                part2 = i.substring(i.indexOf("<"), i.length());
                List1.put(part1, part2);
            }
        }

        br.close();
        fr.close();
        }
  /*  public static void main(String[] argvs) throws Exception {
        ListWordManager t= new ListWordManager();
        t.CreatListWord("C:\\BT lon UET diction\\uet-dictionary\\src\\Sourse\\rua_dat.txt");
        Set<String> set = t.List1.keySet();
        for (String key : set) {
            System.out.println(key + " " + t.List1.get(key));
        }
    }*/
}
