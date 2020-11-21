import java.util.ArrayList;

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

    public Tree[] split() {
        Tree l = this.left;
        Tree r = this.right;
        return new Tree[]{l, r};
    }

    public static Tree addTrees(Tree l, Tree r) {
        Tree res = new Tree();
        res.updateSize(l.size() + r.size());
        res.setLeft(l);
        res.setRight(r);
        return res;
    }

    public static ArrayList<Tree> splitTrees(ArrayList<Tree> tree) {
        boolean success = false;
        while (!success) {
            success = true;
            ArrayList<Tree> temp = new ArrayList<>();

            for (Tree value : tree) {
                if (value.size() > 1) {
                    success = false;
                    Tree[] t = value.split();
                    temp.add(t[0]);
                    temp.add(t[1]);
                } else {
                    temp.add(value);
                }
            }
            tree = temp;
        }

        return tree;
    }

    public static Tree combineTrees(ArrayList<Tree> tree) {
        while (tree.size() > 1) {
            ArrayList<Tree> temp = new ArrayList<>();
            int i = 0;
            while (i < tree.size()) {
                if (i + 1 < tree.size()) {
                    temp.add(Tree.addTrees(tree.get(i), tree.get(i + 1)));
                } else {
                    temp.add(tree.get(i));
                }
                i += 2;
            }
            tree = temp;
        }
        if (tree.isEmpty()) {
            return new Tree();
        } else {
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
        return this.size == 0;
    }

    public static void main(String[] args) {
        ArrayList<Tree> tr = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Tree t = new Tree(i);
            tr.add(t);
        }
        
        Tree res = Tree.combineTrees(tr);
        System.out.println(tr.size());
        System.out.println(res.size());
        ArrayList<Tree> r = new ArrayList<>();
        r.add(res);
        ArrayList<Tree> g = Tree.splitTrees(r);
        for (Tree tree : g) {
            System.out.println("TREE: " + tree.getNodeValue());
        }
    }
}
