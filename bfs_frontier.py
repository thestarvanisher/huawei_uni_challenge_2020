import time
import timeit

def readFile():
    adj = {}
    vconn = {}

    max_n = 0

    with open("competition.txt", "r") as f:
        text = f.read()
        data = text.split("),(")
        #print(len(data))
        for i in data:
            i = i.replace("(", "")
            i = i.replace(")", "")
            i = i.replace("[", "")
            i = i.replace("]", "")

            edge = i.split(",")
            edge = [int(edge[0]), int(edge[1])]

            if edge[0] not in adj:
                adj[edge[0]] = []
            adj[edge[0]].append(edge[1])

            if edge[1] not in adj:
                adj[edge[1]] = []
            adj[edge[1]].append(edge[0])

            if edge[0] not in vconn:
                vconn[edge[0]] = [False, 0, False]
            vconn[edge[0]][1] += 1

            if edge[1] not in vconn:
                vconn[edge[1]] = [False, 0, False]
            vconn[edge[1]][1] += 1

            max_n = max(edge[0], edge[1], max_n)
    #print(max_n + 1)
    return adj, vconn, max_n + 1


def run():
    adj, vconn, n = readFile()
    
    level = []
    answer = []
    start = 0

    level.append(start)
    next_level = []
    lv = 0
    while len(level) > 0:
        lv += 1
        conn = 0
        #print("--> ", len(level))
        
        for node in level[::-1]:
            if vconn[node][0] == False:
                conn += vconn[node][1]

        t1 = time.time()
        for node in level:
            if vconn[node][0] == False:
                answer.append(node)
                vconn[node][0] = True

                for i in adj[node]:
                    if vconn[i][0] == False:
                        vconn[i][1] -= 1
                        if vconn[i][2] == False:
                            vconn[i][2] = True
                            next_level.append(i)
                        else:
                            vconn[node][1] -= 1
                    
        level = next_level
        next_level = []
        t2 = time.time()
        print("Connections: ", conn, " time: ", t2 - t1, "Len: ", len(level))

    print("Levels: ", lv)
    #print(answer)


if __name__ == "__main__":
    #run()
    res = timeit.timeit(run, number=1)
    print(res)