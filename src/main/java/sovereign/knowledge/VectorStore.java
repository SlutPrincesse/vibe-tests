package sovereign.knowledge;

import java.util.*;

public class VectorStore {
    private final Map<String, double[]> vectors = new LinkedHashMap<>();
    private final Map<String, String> texts = new LinkedHashMap<>();

    private double[] embed(String text) {
        double[] v = new double[256];
        for (String tok : text.toLowerCase().split("\\W+")) {
            if (tok.isEmpty()) continue;
            int idx = Math.abs(tok.hashCode()) % v.length;
            v[idx] += 1.0;
        }
        // normalize
        double norm = 0;
        for (double d : v) norm += d*d;
        norm = Math.sqrt(norm);
        if (norm > 0) for (int i=0;i<v.length;i++) v[i]/=norm;
        return v;
    }

    public void add(String id, String text) { texts.put(id, text); vectors.put(id, embed(text)); }
    public List<String> search(String query, int k) {
        double[] qv = embed(query);
        List<Map.Entry<String, Double>> scored = new ArrayList<>();
        for (String id : vectors.keySet()) {
            double[] v = vectors.get(id);
            double dot = 0;
            for (int i=0;i<v.length;i++) dot += v[i]*qv[i];
            scored.add(Map.entry(id, dot));
        }
        scored.sort((a,b) -> Double.compare(b.getValue(), a.getValue()));
        List<String> out = new ArrayList<>();
        for (int i=0;i<Math.min(k, scored.size());i++) out.add(scored.get(i).getKey());
        return out;
    }
    public String get(String id) { return texts.get(id); }
    public Collection<String> ids() { return texts.keySet(); }
}
