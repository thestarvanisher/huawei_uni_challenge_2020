import java.util.ArrayList;
import java.util.Arrays;

public class BFS {
    static ArrayList<Integer> BFS(ArrayList<Integer>[] adj) {
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
}
