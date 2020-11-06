import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parallel_BFS {
    static final int SPLIT_CONSTANT = 10000;

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Integer>[] adj = parseInput("C:\\Users\\Joe\\Desktop\\HPC_Challenge\\src\\competition_data.txt");

        System.out.println("Started BFS");

        long start;
        long end;

//        start = System.nanoTime();
//        System.out.println(bfs(adj));
//        end = System.nanoTime();
//
//        System.out.println(end - start);
//
//        start = System.nanoTime();
//        System.out.println(bfs_2(adj));
//        end = System.nanoTime();

//        System.out.println(end - start);

        start = System.nanoTime();
        System.out.println(bfs_s(adj));
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

    static ArrayList<Integer> bfs_s(ArrayList<Integer>[] adj) {
        List<Integer> answer = Collections.synchronizedList(new ArrayList<>());

        int start = 0;
        List<Integer> level = Collections.synchronizedList(new ArrayList<>());
        level.add(start);

        List<Integer> nextLevel;

        boolean[] visited = new boolean[adj.length];
        Arrays.fill(visited, false);

        while (level.size() > 0) {
            nextLevel = Collections.synchronizedList(new ArrayList<>());

            try {
                processLevel(adj, level, nextLevel, visited, answer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("New Level | " + nextLevel.size());

            level = nextLevel;
        }

        return new ArrayList<>(answer);
    }

    static void processLevel(ArrayList<Integer>[] adj, List<Integer> level, List<Integer> nextLevel, boolean[] visited, List<Integer> answer) throws InterruptedException {
        if (level.size() > SPLIT_CONSTANT) {
            List<Integer>[] partions = split(level);

            Thread t1 = new Thread(() -> {
                try {
                    processLevel(adj, partions[0], nextLevel, visited, answer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t1.start();

            t1.join();

            Thread t2 = new Thread(() -> {
                try {
                    processLevel(adj, partions[1], nextLevel, visited, answer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t2.start();

            t2.join();
        } else {
            for (int node : level) {
                visited[node] = true;

                if (!visited[node]) {
                    answer.add(node);
                }

                for (int j = 0; j < adj[node].size(); j++) {
                    int a = adj[node].get(j);

                    if (!visited[a]) {
                        nextLevel.add(a);
                    }
                }
            }
        }
    }

    static List<Integer>[] split(List<Integer> list) {
        List<Integer> first = Collections.synchronizedList(new ArrayList<>());

        List<Integer> second = Collections.synchronizedList(new ArrayList<>());

        // get size of the list
        int size = list.size();

        // First size)/2 element copy into list
        // first and rest second list
        for (int i = 0; i < size / 2; i++)
            first.add(list.get(i));

        // Second size)/2 element copy into list
        // first and rest second list
        for (int i = size / 2; i < size; i++)
            second.add(list.get(i));

        // return a List of array
        return new List[]{first, second};
    }
}
