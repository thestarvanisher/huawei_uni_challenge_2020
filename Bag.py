from Pennant import Pennant

class Bag:
    data = None
    def __init__(self):
       self.data = []

    def addElement(self, pennant):
        success = False
        i = 0
        current = pennant
        while success == False:
            if i >= len(self.data):
                break

            if self.data[i] == None:
                self.data[i] = current
                success = True
                break

            else:
                self.data[i].joinPennant(current)
                current = self.data[i]
                self.data[i] = None
            
            i += 1
            
        
        if success == False:
            self.data.append(pennant)

    def printBag(self):
        ans = []
        for i in self.data:
            if i == None:
                ans.append(0)
            else:
                ans.append(1)
        
        print(ans[::-1])

 
if __name__ == "__main__":
    b = Bag()
    r = [1, 1, 1, 1, 1]
    for i in r:
        p = Pennant(i)
        b.addElement(p)

    b.printBag()
