package swarmintelligence.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.sum;

/**
 * # TODO user notifactions to improve all queries
 * <p>
 * # TODO make it work: get_context
 * async def statement_get_context_tx(tx, *, statement_id, exclude_ids):  # generiere context
 * return 1
 * r = await tx.run("""PROFILE
 * MATCH (s:Statement{id:$statement_id})
 * UNWIND [1,2,3,4,5] as x
 * return x as A
 * UNION
 * MATCH (s)-[:HAS]->(c:Connection)-->(d:Statement)
 * RETURN d as A
 * <p>
 * """, statement_id=statement_id)
 * print("HAHAHSXXSAD:::    ", *[dict(s for s in await r.fetch(100))])
 * summary: ResultSummary = await r.consume()
 * return summary.profile
 * <p>
 * <p>
 * """
 * async def statement_get_full_context_tx(tx, *, statement_id, username, parent_gens=1, n_parents=3, skip_parents=0,
 * child_gens=1,
 * n_children=8, skip_children=0, ):  # generiere context
 * <p>
 * result = await tx.run(""" """, )
 * <p>
 * return StatementContext()
 * """
 * <p>
 * <p>
 * class TruthCalculator:
 * report_damper = 5  # je höher der wert um so mehr Menschen müssen diese connection reporten damit das zugrunde
 * # liegende Statement einfluss verliert
 * <p>
 * truth_cache = {}  # id:Wahrheitswert paare
 * <p>
 * def R(self, n): return 0.5 if n > 0 else 0
 * <p>
 * def truth_of_node(self, child_truths, child_weights, node_truth_by_vote):
 * R = self.R(len(child_weights))
 * <p>
 * ' = R*v(k) + (1-R)*summe[ i in C_k: w_c(i) * g_k(i) ]/ summe[ i in C_k, g_k(i)]'
 * sum_of_weights = 0
 * sum_weighted_child_truth = 0
 * i = 0
 * for t in child_truths:
 * sum_weighted_child_truth += child_weights[i] * t
 * sum_of_weights += child_weights[i]
 * i += 1
 * out = R * node_truth_by_vote + (1 - R) * sum_weighted_child_truth / sum_of_weights
 * <p>
 * return out
 * <p>
 * def truth_of_node_by_votes(self, votes):
 * return sum([v for v in votes]) / len(votes)
 * <p>
 * def weight_of_connection(self, votes):
 * return sum([v for v in votes]) / len(votes)
 * # return math.e ** (-n_reports / self.report_damper).
 */

class TruthCalculation {
    double report_damper = 5; // je höher der wert um so mehr Menschen müssen diese connection reporten damit das zugrunde
    // liegende Statement einfluss verliert

    Map<String, Double> truth_cache = new HashMap<String, Double>();  // id:Wahrheitswert paare

    public double R(Integer n) {
        return n > 0 ? 0.5 : 0;
    }

    public double truth_of_node(List<Double> child_truths, List<Double> child_weights, Double node_truth_by_vote) {
        // = R*v(k) + (1-R)*summe[ i in C_k: w_c(i) * g_k(i) ]/ summe[ i in C_k, g_k(i)]
        Double r = R(child_weights.size());
        Double sum_of_weights = 0.0;
        Double sum_weighted_child_truth = 0.0;

        for (int i = 0; i < child_weights.size(); i++) {

            sum_weighted_child_truth += child_weights.get(i) * child_truths.get(i);
            sum_of_weights += child_weights.get(i);

        }

        double out = r * node_truth_by_vote + (1 - r) * sum_weighted_child_truth / sum_of_weights;

        return out;
    }

    double truth_of_node_by_votes(List<Integer> votes) {

        double out = (double)votes.stream().reduce(0, Integer::sum) / votes.size();
        return out;
    }

    double weight_of_connection(List<Integer> votes) {
        double out = (double)votes.stream().reduce(0, Integer::sum) / votes.size();
        return out;
    }
}
