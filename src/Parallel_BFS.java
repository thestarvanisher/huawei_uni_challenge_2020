import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parallel_BFS {

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Integer>[] adj = parseInput("C:\\Users\\Joe\\Desktop\\HPC_Challenge\\src\\data_toy.txt");

        long start;
        long end;

        start = System.nanoTime();
        System.out.println(bfs(adj));
        end = System.nanoTime();

        System.out.println(end - start);

        start = System.nanoTime();
        System.out.println(bfs_2(adj));
        end = System.nanoTime();

        System.out.println(end - start);
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

    static ArrayList<Integer> bfs(ArrayList<Integer>[] adj) {
        ArrayList<Integer> answer = new ArrayList<>();

        int start = 0;
        ArrayList<Integer> level = new ArrayList<>();

        level.add(start);

        boolean[] visited = new boolean[adj.length];
        Arrays.fill(visited, false);

        while (level.size() > 0) {
            int node = level.remove(0);

            if (!visited[node]) {
                answer.add(node);
                visited[node] = true;
            }

            adj[node].forEach(a -> {
                if (!visited[a]) {
                    level.add(a);
                }
            });
        }

        return answer;
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
}
