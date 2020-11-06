from Pennant import Pennant

class Bag:
    data = None
    def __init__(self, n = 0):
        if n == 0:
            self.data = [None]
        else:
            self.data = [None] * n

    def addElement(self, pennant):
        success = False
        i = 0
        current = pennant
        #print("Node to add: ", pennant.masterNodeValue)
        while success == False:
            
            if i >= len(self.data):
                break
            #print("--")
            #current.printPennant()
            if self.data[i] == None:
                self.data[i] = current
                success = True
                #print(self.data[i].printPennant())
                break

            else:
                self.data[i].joinPennant(current)
                current = self.data[i]
                self.data[i] = None
            
            
            i += 1
            
        if success == False:
            self.data.append(current)

        #current.printPennant()

    def split(self):
        bag_left = Bag(len(self.data) - 1)
        bag_right = Bag(len(self.data) - 1)

        #print(len(self.data))
        #self.data = self.data[::-1]
        print("Splitting")

        
        i = len(self.data) - 1

        while i > 0:
            if self.data[i] == None:
                i -= 1

            else:
                p_left, p_right = self.data[i].splitPennant()
                bag_left.data[i - 1] = p_left
                bag_right.data[i - 1] = p_right
                i -= 1
        
        if self.data[0] != None:
            bag_right.addElement(self.data[0])

        
        self = bag_left

        return bag_right



    def printBag(self):
        #ans = []
        for i in self.data:
            if i == None:
                print("None_")
            else:
                print("Node: ", i.masterNodeValue)
                i.printPennant()
                
        
        
if __name__ == "__main__":
    b = Bag()
    r = [1, 2, 3, 4, 5]
    for i in r:
        b.addElement(Pennant(i))

    b1 = b.split()


    b.printBag()
    print("------")
    b1.printBag()
