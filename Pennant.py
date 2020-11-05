class Pennant:
    masterNodeValue = None
    left = None
    right = None
    def __init__(self, val):
        self.masterNodeValue = val

    def joinPennant(self, pennant):
        pennant.right = self.left
        self.left = pennant

    def printPennant(self):
        lv = [self]
        while len(lv) > 0:
            next_lv = []
            for i in lv:
                if i.left != None:
                    next_lv.append(i.left)
                if i.right != None:
                    next_lv.append(i.right)
                print(i.masterNodeValue, end=" ")
            lv = next_lv
            print("\n")

if __name__ == "__main__":
    p1 = Pennant(1)
    p2 = Pennant(2)
    p3 = Pennant(3)
    p4 = Pennant(4)
    p1.joinPennant(p2)
    p3.joinPennant(p4)
    p1.joinPennant(p3)
    p1.printPennant()

