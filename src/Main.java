import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    static ArrayList<Integer>[] parseInput(String filename) throws IOException {
        String[] edges = new String[0];

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            edges = line.split("[)][,][(]");
        }
        br.close();

        edges[0] = edges[0].replace("[(", "");
        edges[edges.length - 1] = edges[edges.length - 1].replace(")]", "");

        int[][] edges_2 = new int[edges.length][2];

        int maxElement = 0;

        for (int i = 0; i < edges.length; i++) {
            String[] nodes = edges[i].split(",");

            int a = Integer.parseInt(nodes[0]);
            int b = Integer.parseInt(nodes[1]);

            maxElement = Math.max(maxElement, a);

            edges_2[i] = new int[]{a, b};
        }

        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[maxElement + 1];

        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayList<>();
        }

        Arrays.stream(edges_2).parallel().forEach(a -> adj[a[0]].add(a[1]));

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
