package bag;

import java.util.*;

public class Tree {
    private Integer nodeValue;
    private Tree left;
    private Tree right;
    int size;

    public Tree() {
        this.nodeValue = null;
        this.size = 0;
    }

    public Tree(int value) {
        this.size = 1;
        this.nodeValue = value;
    }
    
    public void setLeft(Tree t) {
        this.left = t;
    }

    public void setRight(Tree t) {
        this.right = t;
    }

    public Tree getLeft() {
        return this.left;
    }

    public Tree getRight() {
        return this.right;
    }

    public Tree[] split() {
        Tree l = this.left;
        Tree r = this.right;
        Tree[] res = {l, r};
        return res;
    }

    public static Tree addTrees(Tree l, Tree r) {
        Tree res = new Tree();
        res.updateSize(l.size() + r.size());
        res.setLeft(l);
        res.setRight(r);
        return res;
    }

    /*public void addTree(Tree r) {
        Tree temp = new Tree();
        temp.
    }*/

    public static ArrayList<Tree> splitTrees(ArrayList<Tree> tree) {
        boolean success = false;
        int steps = 1;
        while(success == false) {
            //System.out.println("Step: " + steps);
            success = true;
            ArrayList<Tree> temp = new ArrayList<>();

            for(int i = 0; i < tree.size(); i++) {
                if(tree.get(i).size() > 1) {
                    success = false;
                    Tree[] t = tree.get(i).split();
                    temp.add(t[0]);
                    temp.add(t[1]);
                }
                else {
                    temp.add(tree.get(i));
                }
            }
            tree = temp;
            steps++;
        }

        return tree;
    }

    public static Tree combineTrees(ArrayList<Tree> tree) {
        while(tree.size() > 1) {
            ArrayList<Tree> temp = new ArrayList<>();
            int i = 0;
            ///System.out.println("Trees: " + tree.size());
            while(i < tree.size()) {
                if(i + 1 < tree.size()) {
                    //System.out.println("Combining trees");
                    temp.add(Tree.addTrees(tree.get(i), tree.get(i + 1)));
                }
                else {
                    temp.add(tree.get(i));
                }
                i += 2;
            }
            tree = temp;
        }
        if(tree.isEmpty()) {
            return new Tree();
        }
        else {
           return tree.get(0); 
        }
        
    }

    public void updateSize(int size) {
        this.size = size;
    }

    public int size() {
        return this.size;
    }

    public int getNodeValue() {
        return this.nodeValue;
    }

    public boolean isEmpty() {
        if(this.size == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void main(String[] args) {
        ArrayList<Tree> tr = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            Tree t = new Tree(i);
            tr.add(t);
        }
        Tree res = Tree.combineTrees(tr);
        System.out.println(tr.size());
        System.out.println(res.size());
        ArrayList<Tree> r = new ArrayList<>();
        r.add(res);
        ArrayList<Tree> g = Tree.splitTrees(r);
        for(int i = 0; i < g.size(); i++) {
            System.out.println("TREE: " + g.get(i).getNodeValue());
        }

        /*Tree p = new Tree(1);
        Tree p1 = new Tree(2);
        ArrayList<Tree> rr = new ArrayList<>();
        System.out.println(p1.size());
        rr.add(p);
        rr.add(p1);
        Tree t3 = Tree.combineTrees(rr);
        System.out.println("SZ: " + t3.size());*/



    }

}
