import java.util.ArrayList;
import java.util.Arrays;

public class BFS_Sequential {
    static ArrayList<Integer> run(ArrayList<Integer>[] adj) {
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
