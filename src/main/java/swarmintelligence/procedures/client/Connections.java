package swarmintelligence.procedures.client;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Procedure;
import swarmintelligence.models.StringRecord;
import swarmintelligence.models.SuccessRecord;

import java.util.Map;
import java.util.stream.Stream;

public class Connections {

    @Context
    public Log log;

    @Context
    public Transaction tx;

    //@Procedure(name = "swarmintelligence.connection.create")
    @Description("Generate Truth for every Node")
    public Stream<SuccessRecord> create() {

        return Stream.of(new SuccessRecord());
    }
}
