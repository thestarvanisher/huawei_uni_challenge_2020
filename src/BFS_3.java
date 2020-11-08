import bag.Bag;
import bag.Pennant;
import bag.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BFS_3 {
    static final int SPLIT_CONSTANT = 128;

    static final int NUM_NODES = 301;
    static final double PROBABILITY = 0.1;

    public static void main(String[] args) throws IOException {
        long start;
        long end;
        FileWriter fileWriter;
        ArrayList<Integer> answer;

        System.out.println("--- Graph Generation ---");

        //ArrayList<Integer>[] adj = generateGraph(NUM_NODES, PROBABILITY);
        ArrayList<Integer>[] adj = parseInput("competition_data.txt");

        //System.out.println("--- Text Dump ---");
        //fileWriter = new FileWriter(String.format("%d.txt", NUM_NODES));
        //fileWriter.write(getOutput(adj));
        //fileWriter.close();

        /*System.out.println("--- Sequential BFS ---");
        start = System.currentTimeMillis();
        answer = BFS_Sequential.run(adj);
        end = System.currentTimeMillis();

        System.out.println(answer);
        System.out.println(String.format("%d Output Nodes", answer.size()));
        System.out.println(String.format("%dms", end - start));*/

        System.out.println("--- Threaded BFS ---");
        start = System.currentTimeMillis();
        answer = bfs_s(adj);
        end = System.currentTimeMillis();

        System.out.println(answer);
        System.out.println(String.format("%d Output Nodes", answer.size()));
        System.out.println(String.format("%dms", end - start));

        System.out.println("--- Text Dump ---");
        fileWriter = new FileWriter("ans.txt");
        fileWriter.write(answer.toString().replace(" ", ""));
        fileWriter.close();

        System.out.println("--- Done ---");

//        int[] correctAnswer = new int[]{0, 1, 2, 4, 3, 20, 6, 79, 60, 78, 62, 151, 7, 14, 31, 45, 32, 47, 95, 113, 146, 166, 148, 189, 8, 81, 33, 116, 34, 117, 96, 181, 150, 211, 152, 212, 16, 165, 67, 164, 69, 188, 123, 226, 196, 255, 197, 256, 101, 121, 138, 198, 140, 216, 225, 266, 239, 278, 253, 281, 104, 179, 142, 200, 144, 209, 227, 267, 250, 286, 251, 287, 5, 9, 10, 115, 11, 18, 93, 111, 12, 97, 94, 218, 21, 178, 206, 261, 108, 208, 247, 271, 110, 210, 265, 272, 13, 22, 43, 59, 44, 61, 23, 36, 129, 145, 130, 147, 24, 114, 131, 149, 132, 191, 38, 207, 184, 205, 185, 244, 219, 262, 171, 274, 241, 277, 221, 263, 243, 280, 245, 283, 17, 28, 29, 41, 30, 169, 119, 249, 229, 268, 231, 269, 15, 25, 46, 63, 48, 64, 77, 167, 75, 190, 168, 201, 26, 82, 162, 176, 163, 199, 203, 270, 254, 282, 258, 284, 19, 80, 112, 202, 177, 187, 260, 285, 37, 51, 57, 74, 58, 76, 52, 248, 183, 259, 182, 257, 42, 180, 215, 264, 27, 40, 49, 65, 50, 68, 102, 122, 173, 195, 175, 235, 86, 100, 125, 137, 126, 139, 213, 224, 233, 238, 234, 273, 87, 103, 127, 141, 128, 143, 214, 220, 236, 276, 237, 279, 35, 85, 66, 217, 91, 107, 222, 228, 92, 109, 223, 230, 53, 71, 83, 98, 84, 174, 135, 155, 156, 170, 157, 172, 136, 158, 159, 242, 160, 275, 99, 118, 133, 153, 134, 154, 39, 54, 55, 72, 56, 186, 120, 232, 194, 246, 193, 240, 73, 161, 204, 252, 70, 88, 89, 105, 90, 192, 106, 124};
//        int[] computedSolution = answer.stream().mapToInt(i -> i).toArray();

//        org.junit.jupiter.api.Assertions.assertArrayEquals(computedSolution, correctAnswer, "Incorrect Solution.");
    }

    static ArrayList<Integer>[] parseInput(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        String[] edges = new String[0];
        while (scanner.hasNextLine()) {
            edges = scanner.nextLine().split("[)][,][(]");
        }

        scanner.close();

        edges[0] = edges[0].replace("[(", "");
        edges[edges.length - 1] = edges[edges.length - 1].replace(")]", "");

        int maxNode = Arrays.stream(edges).mapToInt(a -> {
            String[] nodes = a.split(",");

            return Math.max(Integer.parseInt(nodes[0]), Integer.parseInt(nodes[1]));
        }).max().orElse(0);

        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[maxNode + 1];
        //System.out.println("MAX: " + maxNode);

        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayList<>();
        }

        Arrays.stream(edges).forEach(a -> {
            String[] nodes = a.split(",");

            int node1 = Integer.parseInt(nodes[0]);
            int node2 = Integer.parseInt(nodes[1]);

            adj[node1].add(node2);
        });

        return adj;
    }

    static String getOutput(ArrayList<Integer>[] adj) {
        return IntStream.range(0, adj.length - 1).filter(a -> adj[a].size() > 0).mapToObj(a -> adj[a].stream().map(b -> String.format("(%d,%d)", a, b)).collect(Collectors.joining(","))).collect(Collectors.joining(",", "[", "]"));
    }

    static ArrayList<Integer> bfs_s(ArrayList<Integer>[] adj) {
        //List<Integer> answer = Collections.synchronizedList(new ArrayList<>());
        List<Integer> answer = new ArrayList<>();
        int start = 0;

        Tree level = new Tree(start);
        
        //level.addElement(new Pennant(start));



        var ref = new Object() {
            Tree nextLevel;
        };

        boolean[] visited = new boolean[adj.length];
        //System.out.println("Adj: " + adj.length);
        Arrays.fill(visited, false);

        boolean[] added = new boolean[adj.length];
        Arrays.fill(added, false);

        answer.add(start);
        int lvv = 1;

        while (!level.isEmpty()) {
            ref.nextLevel = new Tree();
            //System.out.println("-----LEVEL----: " + lvv);

            try {
                SimpleLinkedList<Tree> l = new SimpleLinkedList<>();
                SimpleLinkedList<Tree> r = new SimpleLinkedList<>();

                processLevel(adj, level, l, r, visited, answer, added);

                l.concatWith(r);
                //System.out.println("? " + l.iterator().hasNext());
                ArrayList<Tree> temp = new ArrayList<>(); 
                l.iterator().forEachRemaining(a -> {
                    
                    //System.out.println("__ ADDED: " + a.getNodeValue());
                    //System.out.println("SIZE OF ARRAY: " + answer.size());
                    //ref.nextLevel.addElement(a);
                    if(added[a.getNodeValue()] == false) {
                        answer.add(a.getNodeValue());
                        temp.add(a);
                        added[a.getNodeValue()] = true;
                    }
                    
                });
                //System.out.println(temp.size());
                ref.nextLevel = Tree.combineTrees(temp);
                /*ArrayList<Tree> hh = new ArrayList<>();
                hh.add(ref.nextLevel);
                ArrayList<Tree> test = Tree.splitTrees(hh);
                System.out.println("TEST");
                for(int j = 0; j < test.size(); j++) {
                    if(test.size() == 1 && test.get(0).isEmpty()) {
                        break;
                    }
                System.out.print("" + test.get(j).getNodeValue() + " ");
                }
                System.out.println();*/
                //System.out.println(ref.nextLevel.size());
                //System.out.println("SIZE OF ARRAY: " + answer.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            level = ref.nextLevel;
            lvv++;
        }
        //System.out.println("Finished");
        return new ArrayList<>(answer);
    }

    static void processLevel(ArrayList<Integer>[] adj, Tree level, SimpleLinkedList<Tree> l, SimpleLinkedList<Tree> r, boolean[] visited, List<Integer> answer, boolean[] added) throws InterruptedException {
        if (level.size() > SPLIT_CONSTANT) {
            //System.out.println("BS: " + level.size());
            Tree[] lr = level.split();
            Tree rightTree = lr[1];
            Tree leftTree = lr[0];

            //System.out.println("BS_L: " + leftTree.size());
            //System.out.println("BS_R: " + rightTree.size());

            SimpleLinkedList<Tree> leftLeftLinked = new SimpleLinkedList<>();
            SimpleLinkedList<Tree> leftRightLinked = new SimpleLinkedList<>();

            SimpleLinkedList<Tree> rightLeftLinked = new SimpleLinkedList<>();
            SimpleLinkedList<Tree> rightRightLinked = new SimpleLinkedList<>();

            Thread t1 = new Thread(() -> {
                try {
                    processLevel(adj, rightTree, rightLeftLinked, rightRightLinked, visited, answer, added);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t1.start();
            processLevel(adj, leftTree, leftLeftLinked, leftRightLinked, visited, answer, added);

            t1.join();

            if (leftRightLinked.iterator().hasNext()) {
                leftLeftLinked.concatWith(leftRightLinked);
            }

            if (rightRightLinked.iterator().hasNext()) {
                rightLeftLinked.concatWith(rightRightLinked);
            }

            /*System.out.println("ADDING");

            leftLeftLinked.iterator().forEachRemaining(a -> {
                System.out.print("L:" + a.getNodeValue());
            });

            rightLeftLinked.iterator().forEachRemaining(a -> {
                System.out.print("R:" + a.getNodeValue());
            });
            System.out.println();*/

            l.concatWith(leftLeftLinked);
            r.concatWith(rightLeftLinked);


            //System.out.println(l.iterator().hasNext());
            //System.out.println("At stage combining left:");
           
           
        } else {
            var ref = new Object() {
                boolean flag = false;
            };

            ArrayList<Tree> trees = new ArrayList<>();
            trees.add(level);

            SimpleLinkedList<Tree> linkedList = new SimpleLinkedList<>();

            //System.out.println("------- BAG BEGIN -------");
            //level.printBag();
            //System.out.println("------- BAG END -------");
//            while (!ref.flag) {
//                ref.flag = true;
                /*System.out.println("Press \"ENTER\" to continue...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();*/
//                ArrayList<Tree> nextTrees = new ArrayList<>();

//                  trees.forEach(a -> {
//                    if (a.size() > 1) {
                        
                        
                        //System.out.println(a.getItems().size());
//                        ref.flag = false;

                        /*Bag rightBag = a.splitBag();
                        Bag leftBag = a;*/

//                        Tree[] lr = a.split();

                        /*System.out.println("------- BAG BEGIN -------");
                        leftBag.printBag();
                        System.out.println("------- BAG END -------");
                        System.out.println("------- BAG BEGIN -------");
                        rightBag.printBag();
                        System.out.println("------- BAG END -------");
                        nextBags.add(leftBag);
                        nextBags.add(rightBag);*/

//                        nextTrees.add(lr[0]);
//                        nextTrees.add(lr[1]);

//                    } else {
                        /*System.out.println("------- BAG BEGIN -------");
                        a.printBag();
                        System.out.println("------- BAG END -------");*/
                        //nextBags.add(a);
//                        nextTrees.add(a);
//                    }
//                });

//                trees = nextTrees;
//            }

            trees = Tree.splitTrees(trees);

            //System.out.println("Items which children will be displayed next");
            trees.forEach(a -> {
                //linkedList.add(a.getItems().get(0));
                linkedList.add(a);
                //System.out.println("ITEM: " + a.getNodeValue());
            });
            

            Iterator<Tree> iterator = linkedList.iterator();

            while (iterator.hasNext()) {
                
                int node = iterator.next().getNodeValue();
                //System.out.println("Node: " + node);
                //System.out.println();
                //System.out.print("" + node + " -> ");
                if (!visited[node]) {
                    //answer.add(node);
                    visited[node] = true;
                }

                for (int j = 0; j < adj[node].size(); j++) {
                    int a = adj[node].get(j);

                    if (!visited[a] /*&& !added[a]*/) {
                        //added[a] = true;
                        //System.out.println("N: " + node + " -> " + a + " ");
                        
                        l.add(new Tree(a));
                    }
                }
                //System.out.println();
            }
        }
    }

    static List<Integer>[] split(List<Integer> list) {
        List<Integer> first = Collections.synchronizedList(new ArrayList<>());

        List<Integer> second = Collections.synchronizedList(new ArrayList<>());

        int partitionSize = Math.floorDiv(list.size(), 2);

        Iterator<Integer> iterator = list.iterator();

        while ((partitionSize-- > 0) && iterator.hasNext()) {
            first.add(iterator.next());
        }

        while (iterator.hasNext()) {
            second.add(iterator.next());
        }

        return new List[]{first, second};
    }

    static ArrayList<Integer>[] generateGraph(int nodes, double probability) {
        ArrayList<Integer>[] graph = (ArrayList<Integer>[]) new ArrayList[nodes];

        Random random = new Random(1);

        for (int i = 0; i < nodes; i++) {
            graph[i] = new ArrayList<>();
        }

        IntStream.range(0, nodes - 1).forEach(i -> {

            for (int j = 0; j < nodes; j++) {
                if (random.nextDouble() < probability) {
                    graph[i].add(j);
                }
            }
        });

        return graph;
    }
}
