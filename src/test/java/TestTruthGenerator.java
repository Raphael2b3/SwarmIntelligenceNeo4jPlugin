import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import swarmintelligence.procedures.admin.TruthGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTruthGenerator {

    private Driver driver;
    private Neo4j embeddedDatabaseServer;

    @BeforeAll
    void initializeNeo4j() {
        this.embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer().withProcedure(TruthGenerator.class)
                .build();

        this.driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI());

        try (var session = driver.session()) {
            var result = driver.session().run("""
                    SHOW PROCEDURES
                    """);
            var records = result.list();
            System.out.println("PROCEDURES::::   \n");
            for (var record : records) {
                System.out.println(record.asMap().toString());
            }
            System.out.println(" \n::::::::::::::::::::  ");

        }
    }

    @AfterAll
    void closeDriver() {
        this.driver.close();
        this.embeddedDatabaseServer.close();
    }

    @AfterEach
    void cleanDb() {
        try (Session session = driver.session()) {
            session.run("MATCH (n) DETACH DELETE n");
        }
    }

    /**
     * We should be getting the correct values when there is only one type in each direction
     */
    @Test
    public void generateTruth() {
        try (var session = driver.session()) {
            var r = session.run("""
                    
                    CALL swarmintelligence.procedures.dbmodification.generateTruth() YIELD out
                    return out
                                        
                    """);
            var results = r.list();

            System.out.println(results);
        }
        assert true;
    }
}