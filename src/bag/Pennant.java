package bag;

import java.util.ArrayList;

/**
 * Pennant class
 */
public class Pennant {

    private int nodeValue;
    private Pennant left;
    private Pennant right;

    /**
     * Create the pennant (has one element)
     */
    public Pennant(int value) {
        this.nodeValue = value;
        this.left = null;
        this.right = null;
    }

    /**
     * Get the node value
     *
     * @return The node value
     */
    public int getNodeValue() {
        return this.nodeValue;
    }

    /**
     * Sets the left node
     *
     * @param p - The left node
     */
    public void setLeft(Pennant p) {
        this.left = p;
    }

    /**
     * Sets the right node
     *
     * @param p - The right node
     */
    public void setRight(Pennant p) {
        this.right = p;
    }

    /**
     * Get the left node
     *
     * @return The left node
     */
    public Pennant getLeft() {
        return this.left;
    }

    /**
     * Get the right node
     *
     * @return The right node
     */
    public Pennant getRight() {
        return this.right;
    }

    /**
     * Joins a pennant to the current one. Both pennants must be of the
     * same size.
     *
     * @param p - A pennant of the same size
     */
    public void joinPennant(Pennant p) {
        p.setRight(this.left);
        this.left = p;
    }

    /**
     * Splits the pennant into two. Half of the pennant is preserved in this one,
     * the other half is returned
     *
     * @return The second half of the original pennant
     */
    public Pennant splitPennant() {
        Pennant p = this.left;
        this.left = p.getRight();
        p.setRight(null);
        return p;
    }

    /**
     * Checks if the pennant has one element
     *
     * @return True if the pennant has one element, false otherwise
     */
    public boolean hasOneElement() {
        if (this.left == null && this.right == null) {
            return true;
        } else {
            return false;
        }
    }

    public void printPennant() {
        ArrayList<Pennant> level = new ArrayList<Pennant>();
        level.add(this);
        while (!level.isEmpty()) {
            ArrayList<Pennant> next_level = new ArrayList<Pennant>();
            while (!level.isEmpty()) {
                if (level.get(0).getLeft() != null) {
                    next_level.add(level.get(0).getLeft());
                }
                if (level.get(0).getRight() != null) {
                    next_level.add(level.get(0).getRight());
                }
                System.out.print("" + level.get(0).getNodeValue() + " ");
                level.remove(0);
            }
            level = next_level;
            System.out.println();
        }
    }
}
