import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

/**
 *
 * # TODO calculate Truth-Value with txs
 * async def calc_w(tx):
 *     # leafs getten
 *     r = await tx.run("""
 *         MATCH (a:Statement)
 *         WHERE NOT (a)<-[:OPPOSES|SUPPORTS]-(:Connection)
 *         WITH a
 *         MATCH (:User)-[r:VOTED]->(a)
 *         WITH a, collect(r.value) as votes
 *         WITH a, sum(votes)/count(votes) as truth_voted
 *         RETURN a.id as id, truth_voted as truth
 *     """)
 *     success = await r.value()
 *
 *     # leafs{
 *     #   id,
 *     #   votes = [-1,1,1,1,-1]
 *     # }
 *     #
 *     # for each leaf calculate the voted truth and save it to the cache
 *     #
 *     # get parents from each leaf
 *     #
 *     # for each parent, generate connection weight and connection sign (+ -)
 *     #   -> get each connection and get connection votes
 *     #   -> get parent node id
 *     #
 *     # fo generated connection weight into a list attached to parent node with
 *     #
 *     #
 *     # list of nodes = {id:Truth}
 *     #
 *     #
 *     #
 *     #
 *     #
 *     # list of connections = {id:connection}
 *     #
 *     #
 *     #
 *     #
 *     #
 *     # end
 *
 *     pass
 *
 *
 * # TODO user notifactions to improve all queries
 *
 * # TODO make it work: get_context
 * async def statement_get_context_tx(tx, *, statement_id, exclude_ids):  # generiere context
 *     return 1
 *     r = await tx.run("""PROFILE
 *         MATCH (s:Statement{id:$statement_id})
 *         UNWIND [1,2,3,4,5] as x
 *         return x as A
 *         UNION
 *         MATCH (s)-[:HAS]->(c:Connection)-->(d:Statement)
 *         RETURN d as A
 *
 *     """, statement_id=statement_id)
 *     print("HAHAHSXXSAD:::    ", *[dict(s for s in await r.fetch(100))])
 *     summary: ResultSummary = await r.consume()
 *     return summary.profile
 *
 *
 * """
 * async def statement_get_full_context_tx(tx, *, statement_id, username, parent_gens=1, n_parents=3, skip_parents=0,
 *                                         child_gens=1,
 *                                         n_children=8, skip_children=0, ):  # generiere context
 *
 *     result = await tx.run(""" """, )
 *
 *     return StatementContext()
 * """
 *
 *
 * class TruthCalculator:
 *     report_damper = 5  # je höher der wert um so mehr Menschen müssen diese connection reporten damit das zugrunde
 *     # liegende Statement einfluss verliert
 *
 *     truth_cache = {}  # id:Wahrheitswert paare
 *
 *     def R(self, n): return 0.5 if n > 0 else 0
 *
 *     def truth_of_node(self, child_truths, child_weights, node_truth_by_vote):
 *         R = self.R(len(child_weights))
 *
 *         ' = R*v(k) + (1-R)*summe[ i in C_k: w_c(i) * g_k(i) ]/ summe[ i in C_k, g_k(i)]'
 *         sum_of_weights = 0
 *         sum_weighted_child_truth = 0
 *         i = 0
 *         for t in child_truths:
 *             sum_weighted_child_truth += child_weights[i] * t
 *             sum_of_weights += child_weights[i]
 *             i += 1
 *         out = R * node_truth_by_vote + (1 - R) * sum_weighted_child_truth / sum_of_weights
 *
 *         return out
 *
 *     def truth_of_node_by_votes(self, votes):
 *         return sum([v for v in votes]) / len(votes)
 *
 *     def weight_of_connection(self, votes):
 *         return sum([v for v in votes]) / len(votes)
 *         # return math.e ** (-n_reports / self.report_damper).
 */
public class TruthGenerator {


    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    @Context
    public GraphDatabaseService db;

    @Procedure(name = "swarmintelligence.generateTruth")
    @Description("Generate Truth for every Node")
    public Stream<String> generateTruth() {

        try (var tx = db.beginTx()) {

                tx.execute("MATCH (a:Statement) return a");
        }
        return Stream.of("Worked");
    }

    /**
     * Adds the distinct type of a relationship to the given List<String>
     *
     * @param list         the list to add the distinct relationship type to
     * @param relationship the relationship to get the name() from
     */
    private void AddDistinct(List<String> list, Relationship relationship) {
        AddDistinct(list, relationship.getType().name());
    }

    /**
     * Adds an item to a List only if the item is not already in the List
     *
     * @param list the list to add the distinct item to
     * @param item the item to add to the list
     */
    private <T> void AddDistinct(List<T> list, T item) {
        if (!list.contains(item))
            list.add(item);
    }

    /**
     * This is the output record for our search procedure. All procedures
     * that return results return them as a Stream of Records, where the
     * records are defined like this one - customized to fit what the procedure
     * is returning.
     * <p>
     * These classes can only have public non-final fields, and the fields must
     * be one of the following types:
     *
     * <ul>
     *     <li>{@link String}</li>
     *     <li>{@link Long} or {@code long}</li>
     *     <li>{@link Double} or {@code double}</li>
     *     <li>{@link Number}</li>
     *     <li>{@link Boolean} or {@code boolean}</li>
     *     <li>{@link Node}</li>
     *     <li>{@link org.neo4j.graphdb.Relationship}</li>
     *     <li>{@link org.neo4j.graphdb.Path}</li>
     *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
     *     <li>{@link List} of elements of any valid field type, including {@link List}</li>
     *     <li>{@link Object}, meaning any of the valid field types</li>
     * </ul>
     */
    public static class RelationshipTypes {
        // These records contain two lists of distinct relationship types going in and out of a Node.
        public List<String> outgoing;
        public List<String> incoming;

        public RelationshipTypes(List<String> incoming, List<String> outgoing) {
            this.outgoing = outgoing;
            this.incoming = incoming;
        }
    }
}