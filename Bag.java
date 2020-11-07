import java.util.ArrayList;

public class Bag {

    private int size;
    private ArrayList<Pennant> items;

    public Bag() {
        this.size = 0;
        this.items = new ArrayList<Pennant>();
        this.items.add(null);

    }

    public Bag(int n) {
        this.size = 0;
        this.items = new ArrayList<Pennant>();
        for(int i = 0; i < n; i++) {
            this.items.add(null);
        }
    }

    public void addPennant(int position, Pennant p) {
        this.items.set(position, p);
    }

    public ArrayList<Pennant> getItems() {
        return this.items;
    }

    public void addElement(Pennant p) {
        boolean success = false;

        int i = 0;
        while(success == false) {
            if(i >= this.items.size()) {
                break;
            }

            if(this.items.get(i) == null) {
                this.items.set(i, p);
                success = true;
                break;
            }
            else {
                this.items.get(i).joinPennant(p);
                p = this.items.get(i);
                this.items.set(i, null);
                i++;
            }
        }

        if(success == false) {
            this.items.add(p);
        }
    }

    public Bag splitBag() {
        Bag bag_left = new Bag(this.items.size() - 1);
        Bag bag_right = new Bag(this.items.size() - 1);

        for(int i = this.items.size() - 1; i > 0; i--) {
            if(this.items.get(i) == null) {
                continue;
            }
            Pennant p_right = this.items.get(i).splitPennant();
            bag_left.addPennant(i - 1, this.items.get(i));
            bag_right.addPennant(i - 1, p_right);
        }

        if(this.items.get(0) != null) {
            bag_right.addElement(this.items.get(0));
        }

        this.items = bag_left.getItems();

        return bag_right;
    }


    public void printBag() {
        for(int i = 0; i < this.items.size(); i++) {
            if(this.items.get(i) == null) {
                System.out.println("No Element");
            }
            else {
                this.items.get(i).printPennant();
            }
            System.out.println("------");
        }
    }


    public static void main(String[] args) {
        System.out.println("Begin");

        Pennant p1 = new  Pennant(1);
        Pennant p2 = new Pennant(2);
        Pennant p3 = new Pennant(3);
        Pennant p4 = new Pennant(4);
        Pennant p5 = new Pennant(5);

        Bag b1 = new Bag();
        b1.addElement(p1);
        b1.addElement(p2);
        b1.addElement(p3);
        b1.addElement(p4);
        b1.addElement(p5);

        //b1.printBag();

        b1.splitBag();
        b1.printBag();

    }
}