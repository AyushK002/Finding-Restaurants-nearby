import java.util.*;
import java.io.*;

//------------------- PAIR CLASS -------------------------------
class Pair<A, B>{
	public A First;
	public B Second;
	public Pair(){

    }
	public Pair(A _first, B _second) {
        this.First = _first;
        this.Second = _second;
    }
    public A get_first() {
        return First;
    }
    public B get_second() {
        return Second;
    }
}

//-------------------- TREENODE CLASS ----------------------------
class TreeNode {
    TreeNode par;
    TreeNode left;
    TreeNode right;
    Pair<Integer, Integer> val;
    Pair<Integer, Integer> point;
    int level;

    List<Pair<Integer,Integer>> subtree;

    int partition(List<Pair<Integer,Integer>> arr, int l, int r, boolean mode)
    {
        if (mode) {
            int x = arr.get(r).Second, i = l;
            for (int j = l; j <= r - 1; j++) {
                if (arr.get(j).Second < x) {
                    Pair<Integer,Integer> temp = arr.get(j);
                    arr.set(j , arr.get(i));
                    arr.set(i , temp);
                    i++;
                }
            }

            Pair<Integer,Integer> temp = arr.get(r);
            arr.set(r , arr.get(i));
            arr.set(i , temp);
            return i;
        } else {
            int x = arr.get(r).First, i = l;
            for (int j = l; j <= r - 1; j++) {
                if (arr.get(j).First < x) {
                    Pair<Integer,Integer> temp = arr.get(j);
                    arr.set(j , arr.get(i));
                    arr.set(i , temp);
                    i++;
                }
            }

            Pair<Integer,Integer> temp = arr.get(r);
            arr.set(r , arr.get(i));
            arr.set(i , temp);
            return i;
        }
    }

    public Pair<Integer, Integer> sorty( List<Pair<Integer, Integer>> subtr, int l, int r, int k) {
        boolean mode = true;
        Pair<Integer, Integer> ans = new Pair<Integer, Integer>();
        
        if (subtr.size() == 2) {
            if (subtr.get(0).Second > subtr.get(1).Second) {
                Pair<Integer, Integer> temp;
                temp = subtr.get(0);
                subtr.set(0, subtr.get(1));
                subtr.set(1, temp);
            }
            ans.First = subtr.get(0).Second;
            ans.Second = subtr.get(1).Second;
            return ans;
        }

        if (l < r) {
            int p = partition(subtr, l, r, mode);
            sorty(subtr, l, p-1, k);
            sorty(subtr, p+1, r, k);
        }

        ans.First = subtr.get(0).Second;
        ans.Second = subtr.get(subtr.size()-1).Second;
        return ans;
    }

    public Pair<Integer, Integer> sortx( List<Pair<Integer, Integer>> subtr, int l, int r, int k) {
        boolean mode = false;

        Pair<Integer, Integer> ans = new Pair<Integer, Integer>();
        
        if (subtr.size() == 2) {
            if (subtr.get(0).Second > subtr.get(1).Second) {
                Pair<Integer, Integer> temp;
                temp = subtr.get(0);
                subtr.set(0, subtr.get(1));
                subtr.set(1, temp);
            }
            ans.First = subtr.get(0).First;
            ans.Second = subtr.get(1).First;
            return ans;
        }

        if (l < r) {  
            int p = partition(subtr, l, r, mode);
            sortx(subtr, l, p-1, k);
            sortx(subtr, p+1, r, k);
        }

        ans.First = subtr.get(0).First;
        ans.Second = subtr.get(subtr.size()-1).First;
        return ans;
    }

    public void maketree() {

        if(subtree.size() > 1) {

            int k = (subtree.size()-1)/2;

            if (level%2 == 1) {
                val = sorty(subtree, 0, subtree.size()-1, k);
            } else {
                val = sortx(subtree, 0, subtree.size()-1, k);
            }
            //System.out.println(val);
            left = new TreeNode();
            right = new TreeNode();
            left.par = right.par = this;
            left.level = right.level = level+1;
            left.subtree = subtree.subList(0, k+1);
            right.subtree = subtree.subList(k+1, subtree.size());

            left.maketree();
            right.maketree();
        } else {
            point = subtree.get(0);
            return;
        }
    }

    public int findres (Pair<Integer, Integer> p , boolean strike1, boolean strike2) {
        
        //int ans = 0;

        if (point != null) {
            int a = point.First - p.First;
            int b = point.Second - p.Second;
            if ( (a*a <= 10000) & (b*b <= 10000) ) {
                return 1;
            } else {
                return 0;
            }
        } else {

            if (strike1 & strike2) {
                return subtree.size();
            }

            if (level % 2 == 0) {
                if ( (p.First - 100 < val.First)&(val.Second < p.First + 100) ) {
                    strike1 = true;
                } else if ( (val.Second < p.First - 100)||(val.First > p.First + 100) ) {
                    return 0;
                }
            } else {
                if ( (p.Second - 100 < val.First)&(val.Second < p.Second + 100) ) {
                    strike2 = true;
                } else if ( (val.Second < p.Second - 100)||(val.First > p.Second + 100) ) {
                    return 0;
                }
            }
            return left.findres(p, strike1, strike2) + right.findres(p, strike1, strike2);
        }
    }


}


//--------------------- KDTREE CLASS -------------------------------
public class kdtree {

    TreeNode rootnode;

    public Pair<Integer, Integer> addto(String s) {
        
        String n1 = "";
        int i = 0;
        while (s.charAt(i) != ',') {
            n1 += s.charAt(i);
            i++;
        }
        int a = Integer.parseInt(n1);
        int b = Integer.parseInt(s.substring(i+1));

        Pair <Integer,Integer> p = new Pair<Integer, Integer>(a,b);

        return p;
    }

    public void readrestaurants() throws FileNotFoundException {
        File fr = new File("restaurants.txt");
        Scanner dataR = new Scanner(fr);
        dataR.nextLine();
        List <Pair<Integer, Integer>> coords = new ArrayList<Pair<Integer,Integer>>();
        while (dataR.hasNextLine()) {
            String s = dataR.nextLine();
            coords.add(addto(s));
            //System.out.println(addto(s).First + " " + addto(s).Second);
        }
        dataR.close();
    
        rootnode = new TreeNode();
        rootnode.level = 0;
        rootnode.par = null;
        rootnode.subtree = coords;
        rootnode.maketree();
    }

    public void readquery() throws FileNotFoundException, IOException {
        File fq = new File("queries.txt");
        Scanner dataQ = new Scanner(fq);
        dataQ.nextLine();
        List <Pair<Integer, Integer>> coords = new ArrayList<Pair<Integer,Integer>>();
        while (dataQ.hasNextLine()) {
            String s = dataQ.nextLine();
            coords.add(addto(s));
            //System.out.println(addto(s).First + " " + addto(s).Second);
        }
        dataQ.close();

        // Now we solve for each query, one by one :

        FileOutputStream fo = new FileOutputStream("output.txt",true);
        PrintStream p = new PrintStream(fo);
        int i = 0;
        while(i < coords.size()) {
            p.println( rootnode.findres(coords.get(i),false,false) );
            i++;
        }

        fo.close();
    }

    // This is the main function of this file. All the master commands go here.

    public static void main(String[] args) throws FileNotFoundException, IOException {
        kdtree newtree = new kdtree();
        newtree.readrestaurants();
        newtree.readquery();
    }
        
}