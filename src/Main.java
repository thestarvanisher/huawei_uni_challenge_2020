import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static int SPLIT_CONSTANT = 128;

    public static void main(String[] args) throws IOException {
        FileWriter fileWriter;
        ArrayList<Integer> answer;

        long start;
        long end;

        start = System.currentTimeMillis();
        ArrayList<Integer>[] adj = parseInput(args[0]);
        end = System.currentTimeMillis();

        System.out.println(adj.length);

        System.out.println(String.format("Parse Time: %d", end - start));

        start = System.currentTimeMillis();
        answer = bfs(adj);
        end = System.currentTimeMillis();

        System.out.println(String.format("Run Time: %d", end - start));

        start = System.currentTimeMillis();
        fileWriter = new FileWriter("ans.txt");
        fileWriter.write(answer.toString().replace(" ", ""));
        fileWriter.close();
        end = System.currentTimeMillis();

        System.out.println(String.format("Write Time: %d", end - start));
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

    static ArrayList<Integer> bfs(ArrayList<Integer>[] adj) {
        List<Integer> answer = new ArrayList<>();
        int start = 0;

        Tree level = new Tree(start);

        var ref = new Object() {
            Tree nextLevel;
        };

        boolean[] visited = new boolean[adj.length];
        Arrays.fill(visited, false);

        boolean[] added = new boolean[adj.length];
        Arrays.fill(added, false);

        answer.add(start);

        while (!level.isEmpty()) {
            ref.nextLevel = new Tree();

            try {
                SimpleLinkedList<Tree> l = new SimpleLinkedList<>();
                SimpleLinkedList<Tree> r = new SimpleLinkedList<>();

                processLevel(adj, level, l, r, visited, answer, added);

                l.concatWith(r);

                ArrayList<Tree> temp = new ArrayList<>();
                l.iterator().forEachRemaining(a -> {
                    if (!added[a.getNodeValue()]) {
                        answer.add(a.getNodeValue());
                        temp.add(a);
                        added[a.getNodeValue()] = true;
                    }

                });
                ref.nextLevel = Tree.combineTrees(temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            level = ref.nextLevel;
        }
        return new ArrayList<>(answer);
    }

    static void processLevel(ArrayList<Integer>[] adj, Tree level, SimpleLinkedList<Tree> l, SimpleLinkedList<Tree> r, boolean[] visited, List<Integer> answer, boolean[] added) throws InterruptedException {
        if (level.size() > SPLIT_CONSTANT) {
            Tree[] lr = level.split();
            Tree rightTree = lr[1];
            Tree leftTree = lr[0];

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

            l.concatWith(leftLeftLinked);
            r.concatWith(rightLeftLinked);
        } else {
            ArrayList<Tree> trees = new ArrayList<>();
            trees.add(level);

            SimpleLinkedList<Tree> linkedList = new SimpleLinkedList<>();

            trees = Tree.splitTrees(trees);

            trees.forEach(linkedList::add);


            for (Tree tree : linkedList) {

                int node = tree.getNodeValue();
                if (!visited[node]) {
                    visited[node] = true;
                }

                for (int j = 0; j < adj[node].size(); j++) {
                    int a = adj[node].get(j);

                    if (!visited[a]) {
                        l.add(new Tree(a));
                    }
                }
            }
        }
    }
}
