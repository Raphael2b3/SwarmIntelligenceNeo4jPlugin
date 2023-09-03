package swarmintelligence.procedures.admin;

import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Procedure;
import swarmintelligence.models.StringRecord;


public class TruthGenerator {


    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    @Context
    public GraphDatabaseService db;
    @Procedure(name="swarmintelligence.generateTruth")
    @Description("Generate Truth for every Node")
    public Stream<StringRecord> generateTruth() {

        try (var tx = db.beginTx()) {
            // leafs getten
            var r = tx.execute("""
                    MATCH (a:Statement)
                    WHERE NOT (a)<-[:OPPOSES|SUPPORTS]-(:Connection)
                    WITH a
                    MATCH (:User)-[votes:VOTED]->(a)
                    WITH a, sum(votes.value)/count(votes) as truth_voted
                    RETURN a.id as id, truth_voted as truth
                    """);
        }


        return Stream.of(new StringRecord("Worked"));


        //success = await r.value()

        /** leafs {
         id,
         votes = [-1, 1, 1, 1, -1]
         }
         **/

        //for each leaf calculate the voted truth and save it to the cache

        //get parents from each leaf

        //for each parent, generate connection weight and connection sign(+-)
        //   ->get each connection and get connection votes
        //   ->get parent node id

        //fo generated connection weight into a list attached to parent node with

        // list of nodes = {id:Truth}

        // list of connections = {id:connection}

    }


}

