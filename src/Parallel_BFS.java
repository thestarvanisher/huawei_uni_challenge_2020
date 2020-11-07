import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parallel_BFS {
    static final int SPLIT_CONSTANT = 128;

    static final int NUM_NODES = 5000;
    static final double PROBABILITY = 0.05;

    public static void main(String[] args) throws IOException {
        long start;
        long end;
        FileWriter fileWriter;
        ArrayList<Integer> answer;

        System.out.println("--- Graph Generation ---");

//        ArrayList<Integer>[] adj = parseInput(args[0]);
        ArrayList<Integer>[] adj = generateGraph(NUM_NODES, PROBABILITY);

        System.out.println("--- Text Dump ---");
        fileWriter = new FileWriter(String.format("%d.txt", NUM_NODES));
        fileWriter.write(getOutput(adj));
        fileWriter.close();

        System.out.println("--- Sequential BFS ---");
        start = System.currentTimeMillis();
        answer = bfs_2(adj);
        end = System.currentTimeMillis();

        System.out.println(answer);
        System.out.println(String.format("%d Output Nodes", answer.size()));
        System.out.println(String.format("%dms", end - start));

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

            return Integer.parseInt(nodes[0]);
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

    static String getOutput(ArrayList<Integer>[] adj) {
        return IntStream.range(0, adj.length).filter(a -> adj[a].size() > 0).mapToObj(a -> adj[a].stream().map(b -> String.format("(%d,%d)", a, b)).collect(Collectors.joining(","))).collect(Collectors.joining(",", "[", "]"));
    }

    static ArrayList<Integer> bfs_2(ArrayList<Integer>[] adj) {
        ArrayList<Integer> answer = new ArrayList<>();

        int start = 0;
        ArrayList<Integer> level = new ArrayList<>();
        level.add(start);

        ArrayList<Integer> nextLevel;

        boolean[] visited = new boolean[adj.length];
        Arrays.fill(visited, false);

        while (level.size() > 0) {
            nextLevel = new ArrayList<>();

            for (int node : level) {
                if (!visited[node]) {
                    answer.add(node);
                    visited[node] = true;
                }

                for (int j = 0; j < adj[node].size(); j++) {
                    int a = adj[node].get(j);

                    if (!visited[a]) {
                        nextLevel.add(a);
                    }
                }
            }

            level = nextLevel;
        }

        return answer;
    }

    static ArrayList<Integer> bfs_s(ArrayList<Integer>[] adj) {
        List<Integer> answer = Collections.synchronizedList(new ArrayList<>());

        int start = 0;
        ArrayList<Integer> level = new ArrayList<>();
        level.add(start);

        ArrayList<Integer> nextLevel;

        boolean[] visited = new boolean[adj.length];
        Arrays.fill(visited, false);

        boolean[] added = new boolean[adj.length];
        Arrays.fill(visited, false);

        while (level.size() > 0) {
            nextLevel = new ArrayList<>();

            try {
                processLevel(adj, level, nextLevel, visited, answer, added);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            level = nextLevel;
        }

        return new ArrayList<>(answer);
    }

    static void processLevel(ArrayList<Integer>[] adj, ArrayList<Integer> level, ArrayList<Integer> nextLevel, boolean[] visited, List<Integer> answer, boolean[] added) throws InterruptedException {
        if (level.size() > SPLIT_CONSTANT) {
            ArrayList<Integer>[] partions = split(level);

            Thread t1 = new Thread(() -> {
                try {
                    processLevel(adj, partions[0], nextLevel, visited, answer, added);
                    processLevel(adj, partions[1], nextLevel, visited, answer, added);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t1.start();

            t1.join();
        } else {
            for (int node : level) {
                if (!visited[node]) {
                    answer.add(node);
                    visited[node] = true;
                }

                for (int j = 0; j < adj[node].size(); j++) {
                    int a = adj[node].get(j);

                    if (!visited[a] && !added[a]) {
                        added[a] = true;

                        nextLevel.add(a);
                    }
                }
            }
        }
    }

    static ArrayList<Integer>[] split(ArrayList<Integer> list) {
        ArrayList<Integer> first = new ArrayList<>();

        ArrayList<Integer> second = new ArrayList<>();

        int partitionSize = Math.floorDiv(list.size(), 2);

        Iterator<Integer> iterator = list.iterator();

        int a;

        a = partitionSize;

        while ((a-- > 0) && iterator.hasNext()) {
            first.add(iterator.next());
            iterator.remove();
        }

        a = partitionSize;

        while ((a-- > 0) && iterator.hasNext()) {
            second.add(iterator.next());
            iterator.remove();
        }

        return new ArrayList[]{first, second};
    }

    static ArrayList<Integer>[] generateGraph(int nodes, double probability) {
        ArrayList<Integer>[] graph = (ArrayList<Integer>[]) new ArrayList[nodes];

        for (int i = 0; i < nodes; i++) {
            graph[i] = new ArrayList<>();
        }

        IntStream.range(0, nodes - 1).parallel().forEach(i -> {
            for (int j = 0; j < nodes; j++) {
                if (Math.random() < probability) {
                    graph[i].add(j);
                }
            }
        });

        return graph;
    }
}
